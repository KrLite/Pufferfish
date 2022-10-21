package net.krlite.pufferfish.config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.simple_config.SimpleConfig;
import net.krlite.pufferfish.config.simple_config.SimpleConfigHandler;

import java.awt.*;
import java.io.IOException;

public class PuffConfigs {
    public static SimpleConfig CONFIG;
    private static SimpleConfigHandler configs;

    // Crosshair
    public static double crosshairSize, defaultCrosshairSize = 1.0;
    public static double crosshairPuff, defaultCrosshairPuff = 0.35;

    // Keys
    public static int keyLingerTicks, defaultKeyLingerTicks = 10;

    // Colors
    public static final Color TRANSLUCENT = new Color(0, 0, 0, 0);
    public static Color ocean, defaultOcean = new Color(33, 28, 126, 255);
    public static Color scarlet, defaultScarlet = new Color(215, 19, 123, 255);

    private static void createConfigs() {
        configs.addConfig("crosshair_size", new Pair<>(defaultCrosshairSize, "double"));
        configs.addConfig("crosshair_puff", new Pair<>(defaultCrosshairPuff, "double"));

        configs.addConfig("key_linger_ticks", new Pair<>(defaultKeyLingerTicks, "integer"));

        configs.addConfig("color_ocean", new Pair<>(defaultOcean.getRGB(), "integer color"));
        configs.addConfig("color_scarlet", new Pair<>(defaultScarlet.getRGB(), "integer color"));
    }

    private static void assignConfigs() {
        crosshairSize = CONFIG.getOrDefault("crosshair_size", defaultCrosshairSize);
        crosshairPuff = CONFIG.getOrDefault("crosshair_puff", defaultCrosshairPuff);

        keyLingerTicks = CONFIG.getOrDefault("key_linger_ticks", defaultKeyLingerTicks);

        ocean = new Color(CONFIG.getOrDefault("color_ocean", defaultOcean.getRGB()));
        scarlet = new Color(CONFIG.getOrDefault("color_scarlet", defaultScarlet.getRGB()));

        SimpleConfig.LOGGER.info("All " + configs.getConfigList(true).size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }

    private static void saveConfigs() {
        configs.modifyConfig(new Pair<>("crosshair_size", crosshairSize));
        configs.modifyConfig(new Pair<>("crosshair_puff", crosshairPuff));

        configs.modifyConfig(new Pair<>("key_linger_ticks", keyLingerTicks));

        configs.modifyConfig(new Pair<>("color_ocean", ocean.getRGB()));
        configs.modifyConfig(new Pair<>("color_scarlet", scarlet.getRGB()));
    }

    public static void registerConfigs() {
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
