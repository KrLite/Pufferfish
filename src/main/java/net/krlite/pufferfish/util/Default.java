package net.krlite.pufferfish.util;

import java.awt.*;
import java.util.Objects;

public class Default {
    // Enum
    public enum CrosshairStyle {
        VANILLA("Vanilla"),
        PUFFERFISH("Pufferfish");

        private String style;

        CrosshairStyle(String style) {
            this.style = style;
        }

        public String getName() {
            return style;
        }

        public void parse(String value) {
            for ( CrosshairStyle style : CrosshairStyle.values() ) {
                if (Objects.equals(style.getName(), value)) {
                    this.style = style.getName();
                    return;
                }
            }

            this.style = VANILLA.getName();
        }
    }

    // Enum
    public static final CrosshairStyle DEFAULT_CROSSHAIR_STYLE = CrosshairStyle.VANILLA;

    // Integer
    public static final int DEFAULT_KEY_LINGER_TICKS = 10;

    // Double
    public static final double DEFAULT_CROSSHAIR_SIZE = 1.0;
    public static final double DEFAULT_CROSSHAIR_PUFF = 0.35;

    // Color
    public static final Color DEFAULT_PITCH_COLOR = new Color(33, 28, 126, 255);
    public static final Color DEFAULT_YAW_COLOR = new Color(215, 19, 123, 255);

    public static void registerDefaultValues() {

    }
}
