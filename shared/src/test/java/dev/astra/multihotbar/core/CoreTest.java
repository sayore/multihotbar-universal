package dev.astra.multihotbar.core;

public final class CoreTest {
    private static void eq(int expected, int actual) {
        if (expected != actual) throw new AssertionError("expected=" + expected + " actual=" + actual);
    }

    public static void main(String[] args) {
        PressChain c = new PressChain(200);
        long t = 1_000_000_000L;
        eq(0, c.press(0, t, 4));
        eq(1, c.press(0, t + 100_000_000L, 4));
        eq(2, c.press(0, t + 190_000_000L, 4));
        eq(3, c.press(0, t + 280_000_000L, 4));
        eq(3, c.press(0, t + 350_000_000L, 4));
        eq(0, c.press(0, t + 700_000_000L, 4));
        eq(0, c.press(1, t + 750_000_000L, 4));
        eq(29, SlotLayout.controllerSlot(2));
        System.out.println("multihotbar core: PASS");
    }
}
