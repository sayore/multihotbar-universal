package dev.astra.multihotbar.net;

import dev.astra.multihotbar.MultiHotbar;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ActionPayload(byte action, byte hotbarSlot, byte index) implements CustomPacketPayload {
    public static final byte SELECT = 0;
    public static final byte RESTORE_ALL = 1;

    public static final Type<ActionPayload> TYPE = new Type<>(MultiHotbar.id("action"));
    public static final StreamCodec<ByteBuf, ActionPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ActionPayload decode(ByteBuf buf) {
            return new ActionPayload(buf.readByte(), buf.readByte(), buf.readByte());
        }

        @Override
        public void encode(ByteBuf buf, ActionPayload payload) {
            buf.writeByte(payload.action);
            buf.writeByte(payload.hotbarSlot);
            buf.writeByte(payload.index);
        }
    };

    public static ActionPayload select(int slot, int index) {
        return new ActionPayload(SELECT, (byte) slot, (byte) index);
    }

    public static ActionPayload restoreAll() {
        return new ActionPayload(RESTORE_ALL, (byte) -1, (byte) -1);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
