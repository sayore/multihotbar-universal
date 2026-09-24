package dev.astra.multihotbar;

import net.minecraft.resources.ResourceLocation;

public final class MultiHotbar {
    public static final String MOD_ID = "multihotbar";
    public static final int CONTROLLER_BASE_SLOT = 27;
    public static final int HOTBAR_SIZE = 9;
    public static final int CHAIN_WINDOW_MS = 200;

    private MultiHotbar() {}

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
