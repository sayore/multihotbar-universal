package dev.astra.multihotbar.core;

/** Platform-neutral command. Loader modules own actual packet codecs. */
public record ActionMessage(byte action, byte hotbarSlot, byte index) {
    public static final byte SELECT = 0;
    public static final byte RESTORE_ALL = 1;

    public static ActionMessage select(int slot, int index) {
        return new ActionMessage(SELECT, (byte) slot, (byte) index);
    }

    public static ActionMessage restoreAll() {
        return new ActionMessage(RESTORE_ALL, (byte) -1, (byte) -1);
    }
}
