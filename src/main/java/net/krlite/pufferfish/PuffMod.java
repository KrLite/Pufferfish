package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.krlite.pufferfish.config.PuffConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuffMod implements ClientModInitializer {
	public static final String MOD_ID = "puff";
	public static final Logger LOGGER = LoggerFactory.getLogger("Pufferfish");
	public static float crosshairScale;

	@Override
	public void onInitializeClient() {
		PuffConfigs.registerConfigs();
	}
}
