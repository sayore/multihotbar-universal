package dev.astra.multihotbar.mixin.client;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.client.MultiHotbarClientState;
import dev.astra.multihotbar.core.HotbarBundleData;
import dev.astra.multihotbar.net.ActionPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void multihotbar$handleHotbarDepth(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null || mc.screen != null) return;

        for (int slot = 0; slot < MultiHotbar.HOTBAR_SIZE; slot++) {
            ItemStack controller = mc.player.getInventory().getItem(MultiHotbar.CONTROLLER_BASE_SLOT + slot);
            if (!HotbarBundleData.isController(controller)) continue;

            KeyMapping key = mc.options.keyHotbarSlots[slot];
            while (key.consumeClick()) {
                mc.player.getInventory().setSelectedSlot(slot);
                int index = MultiHotbarClientState.press(slot, HotbarBundleData.logicalDepth(controller));
                MultiHotbarClientState.showSelection(slot, index, controller, mc.player.getInventory().getItem(slot));
                ClientNetworkBridge.send(ActionPayload.select(slot, index));
            }
            if (key.isDown() && mc.player.getInventory().getSelectedSlot() == slot) {
                MultiHotbarClientState.keepAlive(slot);
            }
        }
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void multihotbar$normalizeBeforeScreen(Screen screen, CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (screen == null || mc.player == null) return;
        MultiHotbarClientState.reset();
        ClientNetworkBridge.send(ActionPayload.restoreAll());
    }
}
