package dev.astra.multihotbar.client;

import dev.astra.multihotbar.MultiHotbar;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

/** Short-lived, client-owned snapshot of one logical hotbar group. */
public final class PreviewState {
    public static final long DURATION_NANOS = 840_000_000L;
    private static final long FADE_IN_NANOS = 55_000_000L;
    private static final long FADE_OUT_NANOS = 240_000_000L;

    private final int hotbarSlot;
    private final int selectedIndex;
    private final List<ItemStack> items;
    private long shownAt;

    public PreviewState(int hotbarSlot, int selectedIndex, List<ItemStack> items, long shownAt) {
        if (hotbarSlot < 0 || hotbarSlot >= MultiHotbar.HOTBAR_SIZE
                || selectedIndex < 0 || selectedIndex >= items.size()) {
            throw new IllegalArgumentException("Invalid preview selection");
        }
        List<ItemStack> snapshot = new ArrayList<>(items.size());
        for (ItemStack item : items) snapshot.add(item.copy());
        this.hotbarSlot = hotbarSlot;
        this.selectedIndex = selectedIndex;
        this.items = List.copyOf(snapshot);
        this.shownAt = shownAt;
    }

    public void refresh(long now) {
        this.shownAt = now;
    }

    public int hotbarSlot() { return hotbarSlot; }
    public int selectedIndex() { return selectedIndex; }
    public List<ItemStack> items() { return items; }
    public long shownAt() { return shownAt; }
    public ItemStack selectedItem() { return items.get(selectedIndex); }

    public int previousIndex() {
        for (int i = selectedIndex - 1; i >= 0; i--) if (!items.get(i).isEmpty()) return i;
        return -1;
    }

    public int nextIndex() {
        for (int i = selectedIndex + 1; i < items.size(); i++) if (!items.get(i).isEmpty()) return i;
        return -1;
    }

    public float alphaAt(long now) {
        long age = now - shownAt;
        if (age < 0 || age >= DURATION_NANOS) return 0;
        if (age < FADE_IN_NANOS) return (float) age / FADE_IN_NANOS;
        long fadeStart = DURATION_NANOS - FADE_OUT_NANOS;
        if (age > fadeStart) return (float) (DURATION_NANOS - age) / FADE_OUT_NANOS;
        return 1;
    }
}
