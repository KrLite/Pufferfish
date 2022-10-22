package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.event.InputEventHandler;
import net.krlite.pufferfish.render.CrosshairPuffer;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.krlite.pufferfish.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuffMod implements ClientModInitializer {
	public static final String MOD_ID = "puff";
	public static final Logger LOGGER = LoggerFactory.getLogger("Pufferfish");

	@Override
	public void onInitializeClient() {
		PuffConfigs.registerConfigs();
		AxisLocker.registerAxisMaps();
		InputEventHandler.init();
	}
}
