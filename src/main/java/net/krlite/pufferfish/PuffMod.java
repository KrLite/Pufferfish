package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.event.InputEventHandler;
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
		ChatUtil.init();
	}
}
