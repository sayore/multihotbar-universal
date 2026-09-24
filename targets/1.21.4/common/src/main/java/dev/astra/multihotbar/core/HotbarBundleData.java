package dev.astra.multihotbar.core;

import dev.astra.multihotbar.item.HotbarBundleItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;

public final class HotbarBundleData {
    public static final int SLOTS = 9;
    private static final String ACTIVE = "multihotbar_active";

    private HotbarBundleData() {}

    public static boolean isController(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof HotbarBundleItem;
    }

    public static NonNullList<ItemStack> items(ItemStack controller) {
        NonNullList<ItemStack> out = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        controller.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(out);
        return out;
    }

    public static void setItems(ItemStack controller, NonNullList<ItemStack> items) {
        controller.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    public static int active(ItemStack controller) {
        CompoundTag tag = controller.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return Mth.clamp(tag.getInt(ACTIVE), 0, SLOTS - 1);
    }

    public static void setActive(ItemStack controller, int index) {
        int safe = Mth.clamp(index, 0, SLOTS - 1);
        CustomData.update(DataComponents.CUSTOM_DATA, controller, tag -> tag.putInt(ACTIVE, safe));
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
