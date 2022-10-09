package net.krlite.pufferfish.config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.PuffMod;

public class PuffConfigs {
    public static SimpleConfig CONFIG;
    private static PuffConfigProvider configs;

    public static double lerpDelta;
    public static double crosshairPuff;
    public static double crosshairOriginal = 1.0;
    public static int lingerKeyTicks;

    public static void registerConfigs() {
        configs = new PuffConfigProvider();

        createConfigs();

        CONFIG = SimpleConfig.of(PuffMod.MOD_ID).provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("lerp_delta", 10.0), "double (0.0 = cancel)");
        configs.addKeyValuePair(new Pair<>("crosshair_puff", 1.4), "double (1.0 ~ 2.0)");
        configs.addKeyValuePair(new Pair<>("linger_key_ticks", 10), "int (20 tick = 1 second)");
    }

    private static void assignConfigs() {
        lerpDelta = CONFIG.getOrDefault("lerp_delta", 10.0);
        crosshairPuff = CONFIG.getOrDefault("crosshair_puff", 1.4);
        lingerKeyTicks = CONFIG.getOrDefault("linger_key_ticks", 10);

        SimpleConfig.LOGGER.info("All " + configs.getConfigsList().size() + " configs for " + PuffMod.LOGGER.getName() + " have been set properly.");
    }
}
