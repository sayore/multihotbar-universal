package dev.astra.multihotbar.client;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.core.HotbarBundleData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public final class MultiHotbarClientState {
    private static final PressChain CHAIN = new PressChain(MultiHotbar.CHAIN_WINDOW_MS);
    private static PreviewState preview;

    private MultiHotbarClientState() {}

    public static int press(int hotbarSlot, int depth) {
        return CHAIN.press(hotbarSlot, System.nanoTime(), depth);
    }

    public static void showSelection(int slot, int index, ItemStack controller, ItemStack hand) {
        long now = System.nanoTime();
        List<ItemStack> items;
        if (index > 0 && preview != null && preview.hotbarSlot() == slot
                && now - preview.shownAt() < PreviewState.DURATION_NANOS) {
            items = preview.items();
        } else {
            items = new ArrayList<>(HotbarBundleData.items(controller));
            items.set(HotbarBundleData.active(controller), hand);
        }
        preview = index >= 0 && index < items.size() && !items.get(index).isEmpty()
                ? new PreviewState(slot, index, items, now) : null;
    }

    public static PreviewState preview() {
        if (preview != null && System.nanoTime() - preview.shownAt() >= PreviewState.DURATION_NANOS) preview = null;
        return preview;
    }

    public static void reset() {
        CHAIN.reset();
        preview = null;
    }
}
