package net.krlite.pufferfish.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.MathHelper;

public class ChatUtil {
    public static double backgroundOpacity = 0.0;

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            backgroundOpacity = MathHelper.lerp(0.33, backgroundOpacity, 1.0);
        });
    }

    public static void init() {
        registerEvents();
    }
}
