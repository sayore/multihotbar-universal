package dev.astra.multihotbar.client;

public final class PressChainTest {
    public static void main(String[] args) {
        PressChain c = new PressChain(200);
        long ms = 1_000_000L;
        eq(0, c.press(0, 0, 4));
        eq(1, c.press(0, 120 * ms, 4));
        eq(2, c.press(0, 300 * ms, 4)); // 180 ms since previous press
        eq(3, c.press(0, 450 * ms, 4));
        eq(3, c.press(0, 500 * ms, 4)); // clamped
        eq(0, c.press(0, 800 * ms, 4)); // timed out
        eq(0, c.press(1, 850 * ms, 4)); // different key starts a new chain
        c.reset();
        eq(0, c.press(1, 900 * ms, 1));
        System.out.println("PressChainTest OK");
    }

    private static void eq(int expected, int actual) {
        if (expected != actual) throw new AssertionError("expected " + expected + ", got " + actual);
    }
}
