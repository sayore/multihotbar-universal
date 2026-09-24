package dev.astra.multihotbar.fabric;

import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.client.PreviewHudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public final class MultiHotbarFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> PreviewHudRenderer.render(graphics));
        ClientNetworkBridge.install(payload -> {
            var buf = PacketByteBufs.create();
            payload.write(buf);
            ClientPlayNetworking.send(payload.id(), buf);
        });
    }
}
