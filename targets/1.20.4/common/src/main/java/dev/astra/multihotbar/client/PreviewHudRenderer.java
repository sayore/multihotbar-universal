package dev.astra.multihotbar.client;

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

        int centerX = graphics.guiWidth() / 2 - 80 + state.hotbarSlot() * 20;
        int centerY = graphics.guiHeight() - 56;
        int previous = state.previousIndex();
        int next = state.nextIndex();
        if (previous >= 0) drawIcon(graphics, state.items().get(previous), centerX, centerY - 22, 0.78f, alpha * 0.68f);
        drawIcon(graphics, state.selectedItem(), centerX, centerY, 1.2f, alpha);
        if (next >= 0) drawIcon(graphics, state.items().get(next), centerX, centerY + 22, 0.78f, alpha * 0.68f);
    }

    private static void drawIcon(GuiGraphics graphics, ItemStack item, int centerX, int centerY,
            float scale, float opacity) {
        if (item.isEmpty() || opacity < 0.04f) return;
        int size = Math.round(16 * scale);
        int x = centerX - size / 2;
        int y = centerY - size / 2;
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1);
        graphics.renderItem(item, 0, 0);
        graphics.pose().popPose();
        // The small icon-bound veil dims neighbours and supplies the brief fade without a HUD panel.
        int veil = Math.round((1f - opacity) * 192f);
        if (veil > 0) graphics.fill(x, y, x + size, y + size, veil << 24);
    }
}
