package dev.astra.multihotbar.client;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/** Draws only item icons, anchored to the matching vanilla hotbar slot. */
public final class PreviewHudRenderer {
    private PreviewHudRenderer() {}

    public static void render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null || mc.options.hideGui) return;
        PreviewState state = MultiHotbarClientState.preview();
        if (state == null) return;
        float alpha = state.alphaAt(System.nanoTime());
        if (alpha <= 0) return;

        List<ItemStack> items = state.items();
        int total = items.size();
        int count = 0;
        for (int i = 0; i < total; i++) {
            if (!items.get(i).isEmpty()) count = i + 1;
        }
        if (count == 0) return;

        int centerX = graphics.guiWidth() / 2 - 80 + state.hotbarSlot() * 20;
        int spacing = 20;
        int bottomY = graphics.guiHeight() - 36;

        for (int i = 0; i < count; i++) {
            ItemStack item = items.get(i);
            if (item.isEmpty()) continue;
            int y = bottomY - (count - 1 - i) * spacing;
            boolean selected = (i == state.selectedIndex());
            if (selected) {
                int bgAlpha = Math.round(alpha * 160f);
                if (bgAlpha > 0) {
                    int color = (bgAlpha << 24) | 0x141824;
                    graphics.fill(centerX - 11, y - 11, centerX + 11, y + 11, color);
                    int borderAlpha = Math.round(alpha * 220f);
                    int borderColor = (borderAlpha << 24) | 0x68dcca;
                    graphics.fill(centerX - 11, y - 11, centerX + 11, y - 10, borderColor);
                    graphics.fill(centerX - 11, y + 10, centerX + 11, y + 11, borderColor);
                    graphics.fill(centerX - 11, y - 10, centerX - 10, y + 10, borderColor);
                    graphics.fill(centerX + 10, y - 10, centerX + 11, y + 10, borderColor);
                }
            }
            float scale = selected ? 1.2f : 0.8f;
            float opacity = selected ? alpha : alpha * 0.65f;
            drawIcon(graphics, item, centerX, y, scale, opacity);
        }
    }

    private static void drawIcon(GuiGraphics graphics, ItemStack item, int centerX, int centerY,
            float scale, float opacity) {
        if (item.isEmpty() || opacity < 0.04f) return;
        int size = Math.round(16 * scale);
        int x = centerX - size / 2;
        int y = centerY - size / 2;
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        graphics.renderItem(item, 0, 0);
        graphics.pose().popMatrix();
        // The small icon-bound veil dims neighbours and supplies the brief fade without a HUD panel.
        int veil = Math.round((1f - opacity) * 192f);
        if (veil > 0) graphics.fill(x, y, x + size, y + size, veil << 24);
    }
}
