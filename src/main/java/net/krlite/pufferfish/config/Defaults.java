package net.krlite.pufferfish.config;

import java.awt.*;
import java.util.Objects;

public class Defaults {
    // Enum Classes
    // Hotbar Position
    public enum HotbarPosition {
        CENTER("Center", false),
        LEFT("Left", true);

        private final String name;
        private final boolean left;

        HotbarPosition(String name, boolean left) {
            this.name = name;
            this.left = left;
        }

        public String getName() {
            return this.name;
        }

        public boolean isLeft() {
            return this.left;
        }
    }

    public static HotbarPosition parseHotbarPosition(String value) {
        for ( HotbarPosition name : HotbarPosition.values() ) {
            if ( Objects.equals(name.getName(), value) ) {
                return HotbarPosition.valueOf(value.toUpperCase().replace(" ", "_").replace("-", "_"));
            }
        }

        return HotbarPosition.CENTER;
    }

    // Crosshair Render Style
    public enum CrosshairRenderStyle {
        VANILLA("Vanilla"),
        PUFFERFISH("Pufferfish"),
        OPAQUE("Opaque");

        private final String style;

        CrosshairRenderStyle(String style) {
            this.style = style;
        }

        public String getName() {
            return this.style;
        }
    }

    public static CrosshairRenderStyle parseCrosshairRenderStyle(String value) {
        for ( CrosshairRenderStyle style : CrosshairRenderStyle.values() ) {
            if ( Objects.equals(style.getName(), value) ) {
                return CrosshairRenderStyle.valueOf(value.toUpperCase().replace(" ", "_").replace("-", "_"));
            }
        }

        return CrosshairRenderStyle.VANILLA;
    }

    // Crosshair Style
    public enum CrosshairStyle {
        EMPTY("Empty", 16),
        VANILLA("Vanilla", 0),
        CROSS("Cross", 1),
        SMALL_CROSS("Small Cross", 2),
        X_SHAPE("X-Shape", 3),
        SMALL_X_SHAPE("Small X-Shape", 4),
        AIM("Aim", 5),
        SMALL_AIM("Small Aim", 6),
        CIRCLE("Circle", 7),
        DOT("Dot", 8),
        SMALL_DOT("Small Dot", 9),
        HORIZONTAL("Horizontal", 10);

        private final String style;
        private final int index;

        CrosshairStyle(String style, int index) {
            this.style = style;
            this.index = index;
        }

        public String getName() {
            return this.style;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public static CrosshairStyle parseCrosshairStyle(String value) {
        for ( CrosshairStyle style : CrosshairStyle.values() ) {
            if ( Objects.equals(style.getName(), value) ) {
                return CrosshairStyle.valueOf(value.toUpperCase().replace(" ", "_").replace("-", "_"));
            }
        }

        return CrosshairStyle.VANILLA;
    }

    // Enum
    public static final HotbarPosition DEFAULT_HOTBAR_POSITION = HotbarPosition.CENTER;
    public static final CrosshairRenderStyle DEFAULT_CROSSHAIR_RENDER_STYLE = CrosshairRenderStyle.VANILLA;
    public static final CrosshairStyle DEFAULT_CROSSHAIR_STYLE = CrosshairStyle.VANILLA;

    // Boolean
    public static final boolean DEFAULT_ENABLE_TITLE_ANIMATION = true;
    public static final boolean DEFAULT_ENABLE_CHAT_ANIMATION = true;
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
    public static final Color DEFAULT_CHAT_BACKGROUND_COLOR = new Color(0, 0, 0, 255);

    public static void registerDefaultValues() {

    }
}
