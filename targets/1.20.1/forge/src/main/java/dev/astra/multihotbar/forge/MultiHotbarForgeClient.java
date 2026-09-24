package dev.astra.multihotbar.forge;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.client.PreviewHudRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MultiHotbar.MOD_ID, value = Dist.CLIENT)
public final class MultiHotbarForgeClient {
    private MultiHotbarForgeClient() {}

    @SubscribeEvent
    public static void renderPreview(RenderGuiEvent.Post event) {
        PreviewHudRenderer.render(event.getGuiGraphics());
    }
}
