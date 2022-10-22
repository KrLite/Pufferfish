package net.krlite.pufferfish.config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.simple_config.SimpleConfig;
import net.krlite.pufferfish.config.simple_config.SimpleConfigHandler;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.Default;

import java.awt.*;
import java.io.IOException;

import static net.krlite.pufferfish.util.Default.*;

public class PuffConfigs {
    public static SimpleConfig CONFIG;
    private static SimpleConfigHandler configs;

    // Crosshair
    public static double crosshairSize;
    public static double crosshairPuff;
    public static CrosshairStyle corsshairStyle = CrosshairStyle.VANILLA;

    // Keys
    public static int keyLingerTicks;

    private static void createConfigs() {
        configs.addConfig("crosshair_size", new Pair<>(DEFAULT_CROSSHAIR_SIZE, "double"));
        configs.addConfig("crosshair_puff", new Pair<>(DEFAULT_CROSSHAIR_PUFF, "double"));
        configs.addConfig("crosshair_style", new Pair<>(DEFAULT_CROSSHAIR_STYLE.getName(), "string"));

        configs.addConfig("key_linger_ticks", new Pair<>(DEFAULT_KEY_LINGER_TICKS, "integer"));

        configs.addConfig("color_pitch", new Pair<>(DEFAULT_PITCH_COLOR.getRGB(), "integer color"));
        configs.addConfig("color_yaw", new Pair<>(DEFAULT_YAW_COLOR.getRGB(), "integer color"));
    }

    private static void assignConfigs() {
        crosshairSize = CONFIG.getOrDefault("crosshair_size", DEFAULT_CROSSHAIR_SIZE);
        crosshairPuff = CONFIG.getOrDefault("crosshair_puff", DEFAULT_CROSSHAIR_PUFF);
        corsshairStyle.parse(CONFIG.getOrDefault("crosshair_style", DEFAULT_CROSSHAIR_STYLE.getName()));

        keyLingerTicks = CONFIG.getOrDefault("key_linger_ticks", DEFAULT_KEY_LINGER_TICKS);

        ColorUtil.pitchColor = new Color(CONFIG.getOrDefault("color_pitch", DEFAULT_PITCH_COLOR.getRGB()));
        ColorUtil.yawColor = new Color(CONFIG.getOrDefault("color_yaw", DEFAULT_YAW_COLOR.getRGB()));

        SimpleConfig.LOGGER.info("All " + configs.getConfigList(true).size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }

    private static void saveConfigs() {
        configs.modifyConfig(new Pair<>("crosshair_size", crosshairSize));
        configs.modifyConfig(new Pair<>("crosshair_puff", crosshairPuff));
        configs.modifyConfig(new Pair<>("crosshair_style", corsshairStyle.getName()));

        configs.modifyConfig(new Pair<>("key_linger_ticks", keyLingerTicks));

        configs.modifyConfig(new Pair<>("color_pitch", ColorUtil.pitchColor.getRGB()));
        configs.modifyConfig(new Pair<>("color_yaw", ColorUtil.yawColor.getRGB()));
    }

    public static void registerConfigs() {
        // Put Value Handlers Here
        Default.registerDefaultValues();
        ColorUtil.registerColors();

        configs = new SimpleConfigHandler();
        createConfigs();

        CONFIG = SimpleConfig.of(PuffMod.MOD_ID).provider(configs).request();
        assignConfigs();
    }

    public static void save() throws IOException {
        saveConfigs();
        CONFIG.saveConfig();
    }
}
