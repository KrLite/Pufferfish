package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.CrosshairPuffer;
import net.krlite.pufferfish.util.ScreenEdgeOverlay;
import net.krlite.pufferfish.util.ScreenshotFlasher;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuffMod implements ClientModInitializer {
	public static final String MOD_ID = "puff";
	public static final Logger LOGGER = LoggerFactory.getLogger("Pufferfish");

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			PuffKeys.update();
			ScreenshotFlasher.update();
			ScreenEdgeOverlay.update();
		});

		PuffKeys.registerKeys();
		PuffConfigs.registerConfigs();
	}
}
