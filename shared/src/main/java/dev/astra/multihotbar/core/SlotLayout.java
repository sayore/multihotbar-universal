package dev.astra.multihotbar.core;

/** Vanilla player inventory layout used by supported 1.20.x/1.21.x targets. */
public final class SlotLayout {
    public static final int HOTBAR_SIZE = 9;
    public static final int CONTROLLER_BASE = 27;

    private SlotLayout() {}

    public static int controllerSlot(int hotbarSlot) {
        if (hotbarSlot < 0 || hotbarSlot >= HOTBAR_SIZE) throw new IllegalArgumentException("hotbar slot 0..8");
        return CONTROLLER_BASE + hotbarSlot;
    }
}
