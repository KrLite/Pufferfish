package net.krlite.pufferfish.config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.simple_config.SimpleConfig;
import net.krlite.pufferfish.config.simple_config.SimpleConfigHandler;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.Default;

import java.awt.*;
import java.io.IOException;

import static net.krlite.pufferfish.util.Default.*;

public class PuffConfigs {
    public static SimpleConfig CONFIG;
    private static SimpleConfigHandler configs;



    // Enum
    public static CrosshairRenderStyle corsshairRenderStyle;
    public static CrosshairStyle crosshairStyle;

    // Boolean
    public static boolean enableTitleAnimation;
    public static boolean enableChatAnimation;
    public static boolean enableChatTextShadow;
    public static boolean chatSelfHighlighting;

    // Integer
    public static int keyLingerTicks;

    // Double
    public static double crosshairSize;
    public static double crosshairPuff;

    private static void createConfigs() {
        // General
        configs.addCategory("General");
        configs.addConfig("enable_title_animation", new Pair<>(DEFAULT_ENABLE_TITLE_ANIMATION, "Boolean"));

        // Crosshair
        configs.addCategory("Crosshair");
        configs.addConfig("crosshair_size", new Pair<>(DEFAULT_CROSSHAIR_SIZE, "Double"));
        configs.addConfig("crosshair_puff", new Pair<>(DEFAULT_CROSSHAIR_PUFF, "Double"));
        configs.addConfig("crosshair_render_style", new Pair<>(DEFAULT_CROSSHAIR_RENDER_STYLE.getName(), "Enum | Vanilla, Pufferfish, Opaque"));
        configs.addConfig("crosshair_style", new Pair<>(DEFAULT_CROSSHAIR_STYLE.getName(), "Enum | Vanilla, Cross, Small Cross, X-Shape, Small X-Shape, Aim, Small Aim, Circle, Dot, Small Dot, Horizontal, Empty"));

        // Chat
        configs.addCategory("Chat");
        configs.addConfig("enable_chat_animation", new Pair<>(DEFAULT_ENABLE_CHAT_ANIMATION, "Boolean"));
        configs.addConfig("enable_chat_text_shadow", new Pair<>(DEFAULT_ENABLE_CHAT_TEXT_SHADOW, "Boolean"));
        configs.addConfig("chat_self_highlight", new Pair<>(DEFAULT_CHAT_SELF_HIGHLIGHTING, "Boolean"));
        configs.addConfig("chat_text_color", new Pair<>(Integer.toHexString(DEFAULT_CHAT_TEXT_COLOR.getRGB()).toUpperCase(), "Integer Color"));
        configs.addConfig("chat_background_color", new Pair<>(Integer.toHexString(DEFAULT_CHAT_BACKGROUND_COLOR.getRGB()).toUpperCase(), "Integer Color"));

        // Keys
        configs.addCategory("Keys");
        configs.addConfig("key_linger_ticks", new Pair<>(DEFAULT_KEY_LINGER_TICKS, "Integer"));

        // Colors
        configs.addCategory("Colors");
        configs.addConfig("color_pitch", new Pair<>(Integer.toHexString(DEFAULT_PITCH_COLOR.getRGB()).toUpperCase(), "Integer Color"));
        configs.addConfig("color_yaw", new Pair<>(Integer.toHexString(DEFAULT_YAW_COLOR.getRGB()).toUpperCase(), "Integer Color"));
    }

    private static void assignConfigs() {
        // General
        enableTitleAnimation = CONFIG.getOrDefault("enable_title_animation", DEFAULT_ENABLE_TITLE_ANIMATION);

        // Crosshair
        crosshairSize = CONFIG.getOrDefault("crosshair_size", DEFAULT_CROSSHAIR_SIZE);
        crosshairPuff = CONFIG.getOrDefault("crosshair_puff", DEFAULT_CROSSHAIR_PUFF);
        corsshairRenderStyle = Default.parseCrosshairRenderStyle(CONFIG.getOrDefault("crosshair_render_style", DEFAULT_CROSSHAIR_RENDER_STYLE.getName()));
        crosshairStyle = Default.parseCrosshairStyle(CONFIG.getOrDefault("crosshair_style", DEFAULT_CROSSHAIR_STYLE.getName()));

        // Chat
        enableChatAnimation = CONFIG.getOrDefault("enable_chat_animation", DEFAULT_ENABLE_CHAT_ANIMATION);
        enableChatTextShadow = CONFIG.getOrDefault("enable_chat_text_shadow", DEFAULT_ENABLE_CHAT_TEXT_SHADOW);
        chatSelfHighlighting = CONFIG.getOrDefault("chat_self_highlight", DEFAULT_CHAT_SELF_HIGHLIGHTING);
        ChatUtil.chatTextColor = new Color(
                (int) Long.parseLong(
                        CONFIG.getOrDefault(
                                "chat_text_color",
                                Integer.toHexString(DEFAULT_CHAT_TEXT_COLOR.getRGB()).toUpperCase()
                        ),
                        16
                )
        );
        ChatUtil.chatBackgroundColor = new Color(
                (int) Long.parseLong(
                        CONFIG.getOrDefault(
                                "chat_background_color",
                                Integer.toHexString(DEFAULT_CHAT_BACKGROUND_COLOR.getRGB()).toUpperCase()
                        ),
                        16
                )
        );

        // Keys
        keyLingerTicks = CONFIG.getOrDefault("key_linger_ticks", DEFAULT_KEY_LINGER_TICKS);

        // Colors
        ColorUtil.pitchColor = new Color(
                (int) Long.parseLong(
                        CONFIG.getOrDefault(
                                "color_pitch",
                                Integer.toHexString(DEFAULT_PITCH_COLOR.getRGB()).toUpperCase()
                        ),
                        16)
        );
        ColorUtil.yawColor = new Color(
                (int) Long.parseLong(
                        CONFIG.getOrDefault(
                                "color_yaw",
                                Integer.toHexString(DEFAULT_YAW_COLOR.getRGB()).toUpperCase()
                        ),
                        16
                )
        );

        // Log
        SimpleConfig.LOGGER.info("All " + configs.getConfigList(true).size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }

    private static void saveConfigs() {
        // General
        configs.modifyConfig(new Pair<>("enable_title_animation", enableTitleAnimation));

        // Crosshair
        configs.modifyConfig(new Pair<>("crosshair_size", crosshairSize));
        configs.modifyConfig(new Pair<>("crosshair_puff", crosshairPuff));
        configs.modifyConfig(new Pair<>("crosshair_render_style", corsshairRenderStyle.getName()));
        configs.modifyConfig(new Pair<>("crosshair_style", crosshairStyle.getName()));

        // Chat
        configs.modifyConfig(new Pair<>("enable_chat_animation", enableChatAnimation));
        configs.modifyConfig(new Pair<>("enable_chat_text_shadow", enableChatTextShadow));
        configs.modifyConfig(new Pair<>("chat_self_highlight", chatSelfHighlighting));
        configs.modifyConfig(new Pair<>("chat_text_color", Integer.toHexString(ChatUtil.chatTextColor.getRGB()).toUpperCase()));
        configs.modifyConfig(new Pair<>("chat_background_color", Integer.toHexString(ChatUtil.chatBackgroundColor.getRGB()).toUpperCase()));

        // Keys
        configs.modifyConfig(new Pair<>("key_linger_ticks", keyLingerTicks));

        // Colors
        configs.modifyConfig(new Pair<>("color_pitch", Integer.toHexString(ColorUtil.pitchColor.getRGB()).toUpperCase()));
        configs.modifyConfig(new Pair<>("color_yaw", Integer.toHexString(ColorUtil.yawColor.getRGB()).toUpperCase()));
    }

    private static void registerConfigs() {
        // Put Value Handlers Here
        Default.registerDefaultValues();
        ColorUtil.registerColors();

        configs = new SimpleConfigHandler();
        createConfigs();

        // Put Values Needed to be Initialized Here
        corsshairRenderStyle = CrosshairRenderStyle.VANILLA;
        crosshairStyle = CrosshairStyle.VANILLA;

        CONFIG = SimpleConfig.of(PuffMod.MOD_ID).provider(configs).request();
        assignConfigs();
    }

    public static void save() throws IOException {
        saveConfigs();
        CONFIG.saveConfig();
    }



    public static void init() {
        registerConfigs();
    }
}
