package net.krlite.pufferfish.config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.PuffMod;

public class PuffConfigs {
    public static SimpleConfig CONFIG;
    private static PuffConfigProvider configs;

    public static double lerpDelta;
    public static double crosshairPuff;

    public static void registerConfigs() {
        configs = new PuffConfigProvider();

        createConfigs();

        CONFIG = SimpleConfig.of(PuffMod.MOD_ID).provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("lerp_delta", 25.0), "double(0.0=cancel)");
        configs.addKeyValuePair(new Pair<>("crosshair_puff", 1.9), "double");
    }

    private static void assignConfigs() {
        lerpDelta = CONFIG.getOrDefault("lerp_delta", 25.0);
        crosshairPuff = CONFIG.getOrDefault("crosshair_puff", 1.9);

        SimpleConfig.LOGGER.info("All " + configs.getConfigsList().size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }
}
