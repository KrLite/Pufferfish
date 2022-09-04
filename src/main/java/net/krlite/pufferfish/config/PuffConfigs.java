package net.krlite.pufferfish.config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.PuffMod;

public class PuffConfigs {
    public static SimpleConfig CONFIG;
    private static PuffConfigProvider configs;

    public static void registerConfigs() {
        configs = new PuffConfigProvider();

        createConfigs();

        CONFIG = SimpleConfig.of(PuffMod.MOD_ID).provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        //configs.addKeyValuePair(new Pair<>("default", true), "true/false");
    }

    private static void assignConfigs() {
        String splashMode;

        //DEFAULT = CONFIG.getOrDefault("enable_splash_texts", true);

        SimpleConfig.LOGGER.info("All " + configs.getConfigsList().size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }
}
