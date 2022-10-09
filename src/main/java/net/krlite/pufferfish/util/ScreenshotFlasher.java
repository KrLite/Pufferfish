package net.krlite.pufferfish.util;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.util.math.MathHelper;

public class ScreenshotFlasher {
    public static float flashOpacity = -0.1F;

    public static void set(float opacity) {
        flashOpacity = opacity;
    }

    public static void lerp() {
        flashOpacity = MathHelper.lerp(0.35F, flashOpacity, -0.1F);
    }
}
