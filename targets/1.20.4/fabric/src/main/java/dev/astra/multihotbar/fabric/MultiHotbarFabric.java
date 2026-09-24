package dev.astra.multihotbar.fabric;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.core.HotbarSwitchService;
import dev.astra.multihotbar.item.HotbarBundleItem;
import dev.astra.multihotbar.net.ActionPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public final class MultiHotbarFabric implements ModInitializer {
    public static final Item HOTBAR_BUNDLE = Registry.register(
            BuiltInRegistries.ITEM,
            MultiHotbar.id("hotbar_bundle"),
            new HotbarBundleItem(new Item.Properties()));

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ActionPayload.ID, (server, player, handler, buf, sender) -> {
            ActionPayload payload = ActionPayload.decode(buf);
            server.execute(() -> HotbarSwitchService.handle(player, payload));
        });
    }
}
