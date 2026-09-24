package dev.astra.multihotbar.core;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.net.ActionPayload;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public final class HotbarSwitchService {
    private HotbarSwitchService() {}

    public static void handle(ServerPlayer player, ActionPayload payload) {
        if (payload.action() == ActionPayload.RESTORE_ALL) {
            restoreAll(player);
            return;
        }
        if (payload.action() != ActionPayload.SELECT) return;
        select(player, Byte.toUnsignedInt(payload.hotbarSlot()), Byte.toUnsignedInt(payload.index()));
    }

    public static void select(ServerPlayer player, int hotbarSlot, int targetIndex) {
        if (player.containerMenu != player.inventoryMenu) return;
        if (hotbarSlot < 0 || hotbarSlot >= MultiHotbar.HOTBAR_SIZE) return;
        if (targetIndex < 0 || targetIndex >= HotbarBundleData.SLOTS) return;

        Inventory inv = player.getInventory();
        ItemStack controller = inv.getItem(MultiHotbar.CONTROLLER_BASE_SLOT + hotbarSlot);
        if (!HotbarBundleData.isController(controller)) return;

        int active = HotbarBundleData.active(controller);
        inv.setSelectedSlot(hotbarSlot);
        if (active == targetIndex) return;

        NonNullList<ItemStack> items = HotbarBundleData.items(controller);
        ItemStack target = items.get(targetIndex);
        // Logical slot 0 may intentionally be empty. Empty alternate slots are never selectable.
        if (target.isEmpty() && targetIndex != 0) return;

        ItemStack hand = inv.getItem(hotbarSlot);
        items.set(active, hand);
        inv.setItem(hotbarSlot, target);
        items.set(targetIndex, ItemStack.EMPTY);
        HotbarBundleData.setItems(controller, items);
        HotbarBundleData.setActive(controller, targetIndex);
        sync(player);
    }

    public static void restoreAll(ServerPlayer player) {
        for (int slot = 0; slot < MultiHotbar.HOTBAR_SIZE; slot++) restore(player, slot);
        sync(player);
    }

    public static void restore(ServerPlayer player, int hotbarSlot) {
        Inventory inv = player.getInventory();
        ItemStack controller = inv.getItem(MultiHotbar.CONTROLLER_BASE_SLOT + hotbarSlot);
        if (!HotbarBundleData.isController(controller)) return;

        int active = HotbarBundleData.active(controller);
        if (active == 0) return;

        NonNullList<ItemStack> items = HotbarBundleData.items(controller);
        ItemStack primary = items.get(0); // may legitimately be empty

        ItemStack hand = inv.getItem(hotbarSlot);
        items.set(active, hand);
        inv.setItem(hotbarSlot, primary);
        items.set(0, ItemStack.EMPTY);
        HotbarBundleData.setItems(controller, items);
        HotbarBundleData.setActive(controller, 0);
    }

    private static void sync(ServerPlayer player) {
        player.getInventory().setChanged();
        player.inventoryMenu.broadcastChanges();
        if (player.containerMenu != player.inventoryMenu) player.containerMenu.broadcastChanges();
    }
}
