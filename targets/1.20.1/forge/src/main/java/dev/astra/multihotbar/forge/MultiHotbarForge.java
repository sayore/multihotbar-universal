package dev.astra.multihotbar.forge;

import dev.astra.multihotbar.MultiHotbar;
import dev.astra.multihotbar.client.ClientNetworkBridge;
import dev.astra.multihotbar.core.HotbarSwitchService;
import dev.astra.multihotbar.item.HotbarBundleItem;
import dev.astra.multihotbar.net.ActionPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import java.util.Optional;

@Mod(MultiHotbar.MOD_ID)
public final class MultiHotbarForge {
    private static final String PROTOCOL = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            MultiHotbar.id("main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MultiHotbar.MOD_ID);
    public static final RegistryObject<Item> HOTBAR_BUNDLE = ITEMS.register("hotbar_bundle",
            () -> new HotbarBundleItem(new Item.Properties()));

    public MultiHotbarForge() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CHANNEL.registerMessage(0, ActionPayload.class, ActionPayload::write, ActionPayload::decode,
                (payload, contextSupplier) -> {
                    var context = contextSupplier.get();
                    context.enqueueWork(() -> {
                        ServerPlayer sender = context.getSender();
                        if (sender != null) HotbarSwitchService.handle(sender, payload);
                    });
                    context.setPacketHandled(true);
                }, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        ClientNetworkBridge.install(CHANNEL::sendToServer);
    }
}
