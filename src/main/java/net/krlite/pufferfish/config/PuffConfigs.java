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
    public static CrosshairStyle corsshairStyle = CrosshairStyle.VANILLA;

    // Boolean
    public static boolean enableChatAnimation;
    public static boolean enableChatTextShadow;
    public static boolean chatSelfHighlighting;

    // Integer
    public static int keyLingerTicks;

    // Double
    public static double crosshairSize;
    public static double crosshairPuff;

    private static void createConfigs() {
        // Crosshair
        configs.addCategory("Crosshair");
        configs.addConfig("crosshair_size", new Pair<>(DEFAULT_CROSSHAIR_SIZE, "double"));
        configs.addConfig("crosshair_puff", new Pair<>(DEFAULT_CROSSHAIR_PUFF, "double"));
        configs.addConfig("crosshair_style", new Pair<>(DEFAULT_CROSSHAIR_STYLE.getName(), "string"));

        // Chat
        configs.addCategory("Chat");
        configs.addConfig("enable_chat_animation", new Pair<>(DEFAULT_ENABLE_CHAT_ANIMATION, "boolean"));
        configs.addConfig("enable_chat_text_shadow", new Pair<>(DEFAULT_ENABLE_CHAT_TEXT_SHADOW, "boolean"));
        configs.addConfig("chat_self_highlight", new Pair<>(DEFAULT_CHAT_SELF_HIGHLIGHTING, "boolean"));
        configs.addConfig("chat_text_color", new Pair<>(DEFAULT_CHAT_TEXT_COLOR.getRGB(), "integer color"));
        configs.addConfig("chat_background_color", new Pair<>(DEFAULT_CHAT_BACKGROUND_COLOR.getRGB(), "integer color"));

        // Keys
        configs.addCategory("Keys");
        configs.addConfig("key_linger_ticks", new Pair<>(DEFAULT_KEY_LINGER_TICKS, "integer"));

        // Colors
        configs.addCategory("Colors");
        configs.addConfig("color_pitch", new Pair<>(DEFAULT_PITCH_COLOR.getRGB(), "integer color"));
        configs.addConfig("color_yaw", new Pair<>(DEFAULT_YAW_COLOR.getRGB(), "integer color"));
    }

    private static void assignConfigs() {
        // Crosshair
        crosshairSize = CONFIG.getOrDefault("crosshair_size", DEFAULT_CROSSHAIR_SIZE);
        crosshairPuff = CONFIG.getOrDefault("crosshair_puff", DEFAULT_CROSSHAIR_PUFF);
        corsshairStyle = Default.parse(CONFIG.getOrDefault("crosshair_style", DEFAULT_CROSSHAIR_STYLE.getName()));

        // Chat
        enableChatAnimation = CONFIG.getOrDefault("enable_chat_animation", DEFAULT_ENABLE_CHAT_ANIMATION);
        enableChatTextShadow = CONFIG.getOrDefault("enable_chat_text_shadow", DEFAULT_ENABLE_CHAT_TEXT_SHADOW);
        chatSelfHighlighting = CONFIG.getOrDefault("chat_self_highlight", DEFAULT_CHAT_SELF_HIGHLIGHTING);
        ChatUtil.chatTextColor = new Color(CONFIG.getOrDefault("chat_text_color", DEFAULT_CHAT_TEXT_COLOR.getRGB()));
        ChatUtil.chatBackgroundColor = new Color(CONFIG.getOrDefault("chat_background_color", DEFAULT_CHAT_BACKGROUND_COLOR.getRGB()));

        // Keys
        keyLingerTicks = CONFIG.getOrDefault("key_linger_ticks", DEFAULT_KEY_LINGER_TICKS);

        // Colors
        ColorUtil.pitchColor = new Color(CONFIG.getOrDefault("color_pitch", DEFAULT_PITCH_COLOR.getRGB()));
        ColorUtil.yawColor = new Color(CONFIG.getOrDefault("color_yaw", DEFAULT_YAW_COLOR.getRGB()));

        // Log
        SimpleConfig.LOGGER.info("All " + configs.getConfigList(true).size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }

    private static void saveConfigs() {
        // Crosshair
        configs.modifyConfig(new Pair<>("crosshair_size", crosshairSize));
        configs.modifyConfig(new Pair<>("crosshair_puff", crosshairPuff));
        configs.modifyConfig(new Pair<>("crosshair_style", corsshairStyle.getName()));

        // Chat
        configs.modifyConfig(new Pair<>("enable_chat_animation", enableChatAnimation));
        configs.modifyConfig(new Pair<>("enable_chat_text_shadow", enableChatTextShadow));
        configs.modifyConfig(new Pair<>("chat_self_highlight", chatSelfHighlighting));
        configs.modifyConfig(new Pair<>("chat_text_color", ChatUtil.chatTextColor.getRGB()));
        configs.modifyConfig(new Pair<>("chat_background_color", ChatUtil.chatBackgroundColor.getRGB()));

        // Keys
        configs.modifyConfig(new Pair<>("key_linger_ticks", keyLingerTicks));

        // Colors
        configs.modifyConfig(new Pair<>("color_pitch", ColorUtil.pitchColor.getRGB()));
        configs.modifyConfig(new Pair<>("color_yaw", ColorUtil.yawColor.getRGB()));
    }

    public static void registerConfigs() {
        // Put Value Handlers Here
        Default.registerDefaultValues();
        ColorUtil.registerColors();

        configs = new SimpleConfigHandler();
        createConfigs();

        // Put Values Needed to be Initialized Here
        corsshairStyle = CrosshairStyle.VANILLA;

        CONFIG = SimpleConfig.of(PuffMod.MOD_ID).provider(configs).request();
        assignConfigs();
    }

    public static void save() throws IOException {
        saveConfigs();
        CONFIG.saveConfig();
    }
}
