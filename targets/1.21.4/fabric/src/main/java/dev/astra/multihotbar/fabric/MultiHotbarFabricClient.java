package dev.astra.multihotbar.fabric;

import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.client.PreviewHudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class MultiHotbarFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> PreviewHudRenderer.render(graphics));
        ClientNetworkBridge.install(ClientPlayNetworking::send);
    }
}
