package net.krlite.pufferfish.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ChatUtil {
    public static double backgroundOpacity = 0.0;
    public static Color chatTextColor;

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            backgroundOpacity = MathHelper.lerp(0.33, backgroundOpacity, 1.0);
        });
    }

    public static void init() {
        registerEvents();
    }
}
