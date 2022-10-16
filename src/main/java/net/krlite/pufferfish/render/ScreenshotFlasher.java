package net.krlite.pufferfish.render;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ScreenshotFlasher {
    public static float flashOpacity = -0.1F;
    public static final Identifier FLASH = new Identifier(PuffMod.MOD_ID, "textures/misc/flash.png");

    public static void setOpacity(float opacity) {
        flashOpacity = opacity;
    }

    private static void lerp() {
        flashOpacity = MathHelper.lerp(0.47F, flashOpacity, -0.1F);
    }

    public static void update() {
        lerp();
    }
}
