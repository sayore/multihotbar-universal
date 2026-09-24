package dev.astra.multihotbar.client;

import dev.astra.multihotbar.net.ActionPayload;
import java.util.Objects;
import java.util.function.Consumer;

public final class ClientNetworkBridge {
    private static Consumer<ActionPayload> sender = payload -> {};

    private ClientNetworkBridge() {}

    public static void install(Consumer<ActionPayload> newSender) {
        sender = Objects.requireNonNull(newSender);
    }

    public static void send(ActionPayload payload) {
        sender.accept(payload);
    }
}
