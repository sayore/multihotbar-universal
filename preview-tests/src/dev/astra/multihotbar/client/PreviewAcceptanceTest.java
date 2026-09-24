package dev.astra.multihotbar.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public final class PreviewAcceptanceTest {
    private static ItemStack item(String id) { return new ItemStack(id); }

    public static void main(String[] args) {
        ItemStack controller = item("bundle");
        List<ItemStack> stored = emptySlots();
        stored.set(1, item("pickaxe"));
        stored.set(2, item("axe"));
        controller.bundle(stored, 0);

        int index = MultiHotbarClientState.press(0, 3);
        MultiHotbarClientState.showSelection(0, index, controller, item("sword"));
        assertSlice(0, "sword", -1, 1);

        index = MultiHotbarClientState.press(0, 3);
        MultiHotbarClientState.showSelection(0, index, controller, item("sword"));
        assertSlice(1, "pickaxe", 0, 2);

        // A server inventory update between presses must not reorder the visible slice.
        stored.set(0, item("sword"));
        stored.set(1, ItemStack.EMPTY);
        controller.bundle(stored, 1);
        index = MultiHotbarClientState.press(0, 3);
        MultiHotbarClientState.showSelection(0, index, controller, item("pickaxe"));
        assertSlice(2, "axe", 1, -1);

        ItemStack other = item("other_bundle");
        List<ItemStack> otherItems = emptySlots();
        otherItems.set(1, item("shovel"));
        other.bundle(otherItems, 0);
        index = MultiHotbarClientState.press(1, 2);
        MultiHotbarClientState.showSelection(1, index, other, item("hoe"));
        assertSlice(0, "hoe", -1, 1);
        eq(1, MultiHotbarClientState.preview().hotbarSlot());

        MultiHotbarClientState.showSelection(1, 2, other, item("hoe"));
        if (MultiHotbarClientState.preview() != null) throw new AssertionError("empty target showed preview");

        List<ItemStack> timingItems = List.of(item("sword"), item("pickaxe"));
        PreviewState timing = new PreviewState(0, 0, timingItems, 1_000_000_000L);
        near(0, timing.alphaAt(1_000_000_000L));
        near(1, timing.alphaAt(1_055_000_000L));
        near(1, timing.alphaAt(1_480_000_000L));
        near(0.5f, timing.alphaAt(1_600_000_000L));
        near(0, timing.alphaAt(1_720_000_000L));

        MultiHotbarClientState.showSelection(1, 0, other, item("hoe"));
        MultiHotbarClientState.reset(); // screen opening uses this path
        if (MultiHotbarClientState.preview() != null) throw new AssertionError("screen reset kept preview");
        eq(0, MultiHotbarClientState.press(1, 2));
        System.out.println("PreviewAcceptanceTest OK");
    }

    private static List<ItemStack> emptySlots() {
        return new ArrayList<>(List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY));
    }

    private static void assertSlice(int index, String current, int previous, int next) {
        PreviewState state = MultiHotbarClientState.preview();
        if (state == null) throw new AssertionError("preview absent");
        eq(index, state.selectedIndex());
        if (!current.equals(state.selectedItem().id())) throw new AssertionError("wrong selected item");
        eq(previous, state.previousIndex());
        eq(next, state.nextIndex());
    }

    private static void eq(int expected, int actual) {
        if (expected != actual) throw new AssertionError("expected " + expected + ", got " + actual);
    }

    private static void near(float expected, float actual) {
        if (Math.abs(expected - actual) > 0.001f) throw new AssertionError("expected " + expected + ", got " + actual);
    }
}
