package dev.astra.multihotbar.neoforge;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.client.PreviewHudRenderer;
import dev.astra.multihotbar.core.HotbarSwitchService;
import dev.astra.multihotbar.item.HotbarBundleItem;
import dev.astra.multihotbar.net.ActionPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MultiHotbar.MOD_ID)
public final class MultiHotbarNeoForge {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MultiHotbar.MOD_ID);
    public static final DeferredItem<HotbarBundleItem> HOTBAR_BUNDLE = ITEMS.registerItem(
            "hotbar_bundle", HotbarBundleItem::new, new Item.Properties());

    public MultiHotbarNeoForge(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addListener(this::registerPayloads);
        if (FMLEnvironment.dist == Dist.CLIENT) modBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ClientNetworkBridge.install(payload -> PacketDistributor.SERVER.noArg().send(payload));
        NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post render) -> PreviewHudRenderer.render(render.getGuiGraphics()));
    }

    private void registerPayloads(RegisterPayloadHandlerEvent event) {
        event.registrar(MultiHotbar.MOD_ID).versioned("1").play(ActionPayload.ID, ActionPayload::decode, builder -> builder.server((payload, context) -> {
            context.workHandler().execute(() -> {
                if (context.player().orElse(null) instanceof ServerPlayer player) {
                    HotbarSwitchService.handle(player, payload);
                }
            });
        }));
    }
}
