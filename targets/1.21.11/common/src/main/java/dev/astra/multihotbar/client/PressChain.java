package dev.astra.multihotbar.client;

/** Pure state machine: no Minecraft classes, intentionally easy to test. */
public final class PressChain {
    private final long windowNanos;
    private int slot = -1;
    private int index = 0;
    private long lastPress = Long.MIN_VALUE;

    public PressChain(long windowMillis) {
        if (windowMillis < 1) throw new IllegalArgumentException("windowMillis must be positive");
        this.windowNanos = windowMillis * 1_000_000L;
    }

    public int press(int pressedSlot, long nowNanos, int depth) {
        if (depth < 1) depth = 1;
        boolean chained = pressedSlot == slot && lastPress != Long.MIN_VALUE
                && nowNanos >= lastPress && nowNanos - lastPress <= windowNanos;
        index = chained ? Math.min(index + 1, depth - 1) : 0;
        slot = pressedSlot;
        lastPress = nowNanos;
        return index;
    }

    public void reset() {
        slot = -1;
        index = 0;
        lastPress = Long.MIN_VALUE;
    }

    public int slot() { return slot; }
    public int index() { return index; }
}
