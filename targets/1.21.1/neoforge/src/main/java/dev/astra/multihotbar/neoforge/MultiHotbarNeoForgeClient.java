package dev.astra.multihotbar.neoforge;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.client.PreviewHudRenderer;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(value = MultiHotbar.MOD_ID, dist = Dist.CLIENT)
public final class MultiHotbarNeoForgeClient {
    public MultiHotbarNeoForgeClient(IEventBus modBus) {
        modBus.addListener(this::clientSetup);
        NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post render) -> PreviewHudRenderer.render(render.getGuiGraphics()));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ClientNetworkBridge.install(PacketDistributor::sendToServer);
    }
}
