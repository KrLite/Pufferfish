package net.krlite.pufferfish.config;

import java.awt.*;

public class Default {
    // Enum
    public static final PuffConfig.HotbarPosition DEFAULT_HOTBAR_POSITION = PuffConfig.HotbarPosition.CENTER;
    public static final PuffConfig.CrosshairRenderStyle DEFAULT_CROSSHAIR_RENDER_STYLE = PuffConfig.CrosshairRenderStyle.VANILLA;
    public static final PuffConfig.CrosshairStyle DEFAULT_CROSSHAIR_STYLE = PuffConfig.CrosshairStyle.VANILLA;

    // Boolean
    public static final boolean DEFAULT_ENABLE_TITLE_ANIMATION = true;
    public static final boolean DEFAULT_ENABLE_CHAT_ANIMATION = true;
    public static final boolean DEFAULT_ENABLE_CHAT_TEXT_SHADOW = true;
    public static final boolean DEFAULT_CHAT_SELF_HIGHLIGHTING = false;

    // Integer
    public static final int DEFAULT_KEY_LINGER_TICKS = 10;

    // Double
    public static final double DEFAULT_CROSSHAIR_SIZE = 1.0;
    public static final double DEFAULT_CROSSHAIR_EXPAND_INDEX = 0.35;

    // Color
    public static final Color DEFAULT_CHAT_TEXT_COLOR = new Color(0xFFFFFFFF, true);
    public static final Color DEFAULT_CHAT_BACKGROUND_COLOR = new Color(0xFF000000, true);
    public static final Color DEFAULT_PITCH_COLOR = new Color(0xFF211C7E, true);
    public static final Color DEFAULT_YAW_COLOR = new Color(0xFFD7137B, true);

    public static void registerDefaultValues() {
    }
}
