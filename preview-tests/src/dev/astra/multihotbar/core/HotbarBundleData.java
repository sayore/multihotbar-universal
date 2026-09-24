package dev.astra.multihotbar.core;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public final class HotbarBundleData {
    private HotbarBundleData() {}
    public static List<ItemStack> items(ItemStack controller) { return controller.bundleItems(); }
    public static int active(ItemStack controller) { return controller.active(); }
}
