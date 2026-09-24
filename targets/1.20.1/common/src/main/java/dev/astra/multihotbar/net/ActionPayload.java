package dev.astra.multihotbar.net;

import dev.astra.multihotbar.MultiHotbar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ActionPayload(byte action, byte hotbarSlot, byte index) {
    public static final byte SELECT = 0;
    public static final byte RESTORE_ALL = 1;

    public static final ResourceLocation ID = MultiHotbar.id("action");

    public static ActionPayload decode(FriendlyByteBuf buf) {
        return new ActionPayload(buf.readByte(), buf.readByte(), buf.readByte());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(action);
        buf.writeByte(hotbarSlot);
        buf.writeByte(index);
    }

    public static ActionPayload select(int slot, int index) {
        return new ActionPayload(SELECT, (byte) slot, (byte) index);
    }

    public static ActionPayload restoreAll() {
        return new ActionPayload(RESTORE_ALL, (byte) -1, (byte) -1);
    }

}
