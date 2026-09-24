package net.minecraft.world.item;

import java.util.List;

/** Tiny test double: preview state only needs identity, emptiness, and copying. */
public final class ItemStack {
    public static final ItemStack EMPTY = new ItemStack("");
    private final String id;
    private List<ItemStack> bundleItems;
    private int active;

    public ItemStack(String id) { this.id = id; }
    public boolean isEmpty() { return id.isEmpty(); }
    public ItemStack copy() { return isEmpty() ? EMPTY : new ItemStack(id); }
    public String id() { return id; }
    public List<ItemStack> bundleItems() { return bundleItems; }
    public int active() { return active; }
    public void bundle(List<ItemStack> items, int selected) { bundleItems = items; active = selected; }
}
