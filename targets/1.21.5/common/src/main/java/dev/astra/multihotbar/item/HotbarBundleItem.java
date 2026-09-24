package dev.astra.multihotbar.item;

import dev.astra.multihotbar.core.HotbarBundleData;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

/**
 * Controller item. Logical index 0 is always the paired hotbar item; stored alternates are 1..8.
 * Left-click a carried stack onto the controller to append it. Right-click with an empty cursor to pop the last alternate.
 */
public final class HotbarBundleItem extends Item {
    public HotbarBundleItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack controller, ItemStack carried, Slot slot,
            ClickAction action, Player player, SlotAccess cursor) {
        if (HotbarBundleData.active(controller) != 0) return false;

        if (action == ClickAction.PRIMARY && !carried.isEmpty() && !(carried.getItem() instanceof HotbarBundleItem)) {
            int target = HotbarBundleData.firstEmptyAlternate(controller);
            if (target < 0) return false;
            NonNullList<ItemStack> items = HotbarBundleData.items(controller);
            items.set(target, carried.copy());
            HotbarBundleData.setItems(controller, items);
            cursor.set(ItemStack.EMPTY);
            return true;
        }

        if (action == ClickAction.SECONDARY && carried.isEmpty()) {
            int target = HotbarBundleData.lastFilledAlternate(controller);
            if (target < 0) return false;
            NonNullList<ItemStack> items = HotbarBundleData.items(controller);
            ItemStack out = items.get(target);
            items.set(target, ItemStack.EMPTY);
            HotbarBundleData.setItems(controller, items);
            return cursor.set(out);
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("item.multihotbar.hotbar_bundle.hint").withStyle(ChatFormatting.GRAY));
        NonNullList<ItemStack> items = HotbarBundleData.items(stack);
        int active = HotbarBundleData.active(stack);
        tooltip.accept(Component.literal("1×  ").withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.translatable("item.multihotbar.hotbar_bundle.primary").withStyle(ChatFormatting.WHITE)));
        for (int i = 1; i < HotbarBundleData.SLOTS; i++) {
            ItemStack item = items.get(i);
            if (i == active && item.isEmpty()) {
                tooltip.accept(Component.literal((i + 1) + "×  ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.translatable("item.multihotbar.hotbar_bundle.active").withStyle(ChatFormatting.AQUA)));
            } else if (!item.isEmpty()) {
                tooltip.accept(Component.literal((i + 1) + "×  ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(item.getHoverName().copy().withStyle(ChatFormatting.WHITE)));
            }
        }
    }
}
