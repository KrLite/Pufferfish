package net.krlite.pufferfish.util;

import java.awt.*;
import java.util.Objects;

public class Default {
    // Enum
    public enum CrosshairStyle {
        VANILLA("Vanilla"),
        PUFFERFISH("Pufferfish"),
        OPAQUE("Opaque");

        private final String style;

        CrosshairStyle(String style) {
            this.style = style;
        }

        public String getName() {
            return style;
        }
    }

    public static CrosshairStyle parse(String value) {
        for ( CrosshairStyle style : CrosshairStyle.values() ) {
            if ( Objects.equals(style.getName(), value) ) {
                return CrosshairStyle.valueOf(value.toUpperCase());
            }
        }

        return CrosshairStyle.VANILLA;
    }

    // Enum
    public static final CrosshairStyle DEFAULT_CROSSHAIR_STYLE = CrosshairStyle.VANILLA;

    // Boolean
    public static final boolean DEFAULT_ENABLE_CHAT_ANIMATION = false;
    public static final boolean DEFAULT_ENABLE_CHAT_TEXT_SHADOW = true;
    public static final boolean DEFAULT_CHAT_SELF_HIGHLIGHTING = false;

    // Integer
    public static final int DEFAULT_KEY_LINGER_TICKS = 10;

    // Double
    public static final double DEFAULT_CROSSHAIR_SIZE = 1.0;
    public static final double DEFAULT_CROSSHAIR_PUFF = 0.35;

    // Color
    public static final Color DEFAULT_PITCH_COLOR = new Color(33, 28, 126, 255);
    public static final Color DEFAULT_YAW_COLOR = new Color(215, 19, 123, 255);
    public static final Color DEFAULT_CHAT_TEXT_COLOR = new Color(255, 255, 255, 255);

    public static void registerDefaultValues() {

    }
}
