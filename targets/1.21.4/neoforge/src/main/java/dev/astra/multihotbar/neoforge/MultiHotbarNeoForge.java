package dev.astra.multihotbar.neoforge;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.core.HotbarSwitchService;
import dev.astra.multihotbar.item.HotbarBundleItem;
import dev.astra.multihotbar.net.ActionPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
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
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(ActionPayload.TYPE, ActionPayload.STREAM_CODEC, (payload, context) -> {
            if (context.player() instanceof ServerPlayer player) HotbarSwitchService.handle(player, payload);
        });
    }
}
