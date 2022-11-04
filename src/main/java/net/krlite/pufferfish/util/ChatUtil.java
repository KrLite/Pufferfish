package net.krlite.pufferfish.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ChatUtil {
    public static double chatBackgroundOpacity = 0.0;
    public static Color chatTextColor;
    public static Color chatBackgroundColor;

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            chatBackgroundOpacity = MathHelper.lerp(0.33, chatBackgroundOpacity, 1.0);
        });
    }

    public static void init() {
        registerEvents();
    }
}
