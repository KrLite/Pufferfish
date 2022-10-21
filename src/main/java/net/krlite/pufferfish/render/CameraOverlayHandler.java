package net.krlite.pufferfish.render;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class CameraOverlayHandler {
    private static final float delta = 0.17F;

    private static Identifier identifierBuilder(String textureName) {
        return new Identifier(PuffMod.MOD_ID, "textures/overlay/" + textureName + ".png");
    }

    // Texture
    public static final Identifier NONE = identifierBuilder("transparent");
    public static final Identifier BASIC = identifierBuilder("basic");

    // Color Lerping
    public static Color lerpColor(Color currentColor, Color targetColor) {
        return lerpColor(currentColor, targetColor, 1, 1);
    }

    public static Color lerpColor(Color currentColor, Color targetColor, float currentOpacity, float targetOpacity) {
        return new Color(
                (int) (MathHelper.lerp(
                        delta, currentColor.getRed() / 255.0F,
                        targetColor.getRed() / 255.0F
                ) * 255),
                (int) (MathHelper.lerp(
                        delta, currentColor.getGreen() / 255.0F,
                        targetColor.getGreen() / 255.0F
                ) * 255),
                (int) (MathHelper.lerp(
                        delta, currentColor.getBlue() / 255.0F,
                        targetColor.getBlue() / 255.0F
                ) * 255),
                (int) (MathHelper.lerp(
                        delta, currentOpacity,
                        targetOpacity
                ) * 255)
        );
    }
}
