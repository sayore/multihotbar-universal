package dev.astra.multihotbar.fabric;

import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.client.PreviewHudRenderer;
import dev.astra.multihotbar.MultiHotbar;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class MultiHotbarFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR,
                MultiHotbar.id("quick_select_preview"), (graphics, tickDelta) -> PreviewHudRenderer.render(graphics));
        ClientNetworkBridge.install(ClientPlayNetworking::send);
    }
}
