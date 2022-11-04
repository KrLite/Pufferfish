package net.krlite.pufferfish.util;

import org.checkerframework.checker.units.qual.C;

import java.awt.*;

public class ColorUtil {
    public static final Color TRANSLUCENT = new Color(0, 0, 0, 0);

    // Color
    public static Color pitchColor;
    public static Color yawColor;

    public static Color castAlpha(Color color) {
        return castAlpha(color, 0.0F);
    }

    public static Color castAlpha(Color color, int alphaColor) {
        return castAlpha(color, new Color(alphaColor, true).getAlpha() / 255.0F);
    }

    public static Color castAlpha(Color color, Color alphaColor) {
        return castAlpha(color, alphaColor.getAlpha() / 255.0F);
    }

    public static Color castAlpha(Color color, float alpha) {
        return new Color(
                color.getRed() / 255.0F,
                color.getGreen() / 255.0F,
                color.getBlue() / 255.0F,
                alpha
        );
    }

    public static void registerColors() {
    }
}
