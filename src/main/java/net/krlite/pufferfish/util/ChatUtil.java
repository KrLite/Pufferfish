package net.krlite.pufferfish.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.PuffMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class ChatUtil {
    public static double backgroundOpacity = 0.0;

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            backgroundOpacity = MathHelper.lerp(0.33, backgroundOpacity, MinecraftClient.getInstance().options.textBackgroundOpacity);
        });
    }

    public static void init() {
        registerEvents();
    }
}
