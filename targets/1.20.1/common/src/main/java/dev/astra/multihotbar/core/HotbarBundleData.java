package dev.astra.multihotbar.core;

import dev.astra.multihotbar.item.HotbarBundleItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public final class HotbarBundleData {
    public static final int SLOTS = 9;
    private static final String ACTIVE = "multihotbar_active";
    private static final String ITEMS = "multihotbar_items";

    private HotbarBundleData() {}

    public static boolean isController(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof HotbarBundleItem;
    }

    public static NonNullList<ItemStack> items(ItemStack controller) {
        NonNullList<ItemStack> out = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        CompoundTag data = controller.getTag();
        if (data == null) return out;
        ListTag list = data.getList(ITEMS, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            int slot = Byte.toUnsignedInt(entry.getByte("Slot"));
            if (slot < SLOTS) out.set(slot, ItemStack.of(entry));
        }
        return out;
    }

    public static void setItems(ItemStack controller, NonNullList<ItemStack> items) {
        ListTag list = new ListTag();
        for (int i = 0; i < Math.min(items.size(), SLOTS); i++) {
            ItemStack item = items.get(i);
            if (item.isEmpty()) continue;
            CompoundTag entry = item.save(new CompoundTag());
            entry.putByte("Slot", (byte) i);
            list.add(entry);
        }
        controller.getOrCreateTag().put(ITEMS, list);
    }

    public static int active(ItemStack controller) {
        CompoundTag data = controller.getTag();
        return data == null ? 0 : Mth.clamp(data.getInt(ACTIVE), 0, SLOTS - 1);
    }

    public static void setActive(ItemStack controller, int index) {
        controller.getOrCreateTag().putInt(ACTIVE, Mth.clamp(index, 0, SLOTS - 1));
    }

    /** Highest configured logical position, including the temporary hole occupied by the hotbar item. */
    public static int logicalDepth(ItemStack controller) {
        NonNullList<ItemStack> items = items(controller);
        int highest = active(controller);
        for (int i = 1; i < items.size(); i++) if (!items.get(i).isEmpty()) highest = i;
        return Math.max(1, highest + 1);
    }

    public static int firstEmptyAlternate(ItemStack controller) {
        NonNullList<ItemStack> items = items(controller);
        int active = active(controller);
        for (int i = 1; i < SLOTS; i++) if (i != active && items.get(i).isEmpty()) return i;
        return -1;
    }

    public static int lastFilledAlternate(ItemStack controller) {
        NonNullList<ItemStack> items = items(controller);
        int active = active(controller);
        for (int i = SLOTS - 1; i >= 1; i--) if (i != active && !items.get(i).isEmpty()) return i;
        return -1;
    }
}
