package net.krlite.pufferfish.render;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ScreenshotFlashRenderer {
    public static float flashOpacity = -0.1F;
    public static final Identifier FLASH = new Identifier(PuffMod.MOD_ID, "textures/misc/flash.png");

    public static void setOpacity(float opacity) {
        flashOpacity = opacity;
    }

    private static void lerp() {
        flashOpacity = MathHelper.lerp(0.47F, flashOpacity, -0.1F);
    }

    public static void renderScreenshotFlash() {
        ColoredTextureRenderer.renderColoredOverlay(FLASH, new Color(1.0F, 1.0F, 1.0F, flashOpacity));
        lerp();
    }
}
