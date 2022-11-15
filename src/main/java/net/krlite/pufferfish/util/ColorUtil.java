package net.krlite.pufferfish.util;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.util.math.MathHelper;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;

public class ColorUtil {
    public enum ColorBlendFunc {
        NONE, INVERT, FIRST, SECOND
    }

    public static final Color TRANSLUCENT = new Color(0, 0, 0, 0);

    // Color
    public static Color pitchColor;
    public static Color yawColor;

    public static Color blendColor(Color first, Color second) {
        return blendColor(first, second, ColorBlendFunc.NONE);
    }

    public static Color blendColor(Color first, Color second, ColorBlendFunc colorBlendFunc) {
        switch ( colorBlendFunc ) {
            default -> {
                return new Color(
                        (first.getRed() + second.getRed()) / 2,
                        (first.getGreen() + second.getGreen()) / 2,
                        (first.getBlue() + second.getBlue()) / 2,
                        (first.getAlpha() + second.getAlpha()) / 2
                );
            }

            case INVERT -> {
                return new Color(
                        255 - (first.getRed() + second.getRed()) / 2,
                        255 - (first.getGreen() + second.getGreen()) / 2,
                        255 - (first.getBlue() + second.getBlue()) / 2,
                        255 - (first.getAlpha() + second.getAlpha()) / 2
                );
            }

            case FIRST -> {
                return first;
            }

            case SECOND -> {
                return second;
            }
        }
    }

    public static Color castAlpha(Color color) {
        return castAlpha(color, 0.0F);
    }

    public static Color castAlpha(Color color, int alphaColor) {
        return castAlpha(color, new Color(alphaColor, true).getAlpha() / 255.0F);
    }

    public static Color castAlpha(Color color, Color alphaColor) {
        return castAlpha(color, alphaColor.getAlpha() / 255.0F);
    }

    public static Color castAlpha(Color color, float opacity) {
        return new Color(
                color.getRed() / 255.0F,
                color.getGreen() / 255.0F,
                color.getBlue() / 255.0F,
                opacity
        );
    }

    public static void registerColors() {
    }
}
