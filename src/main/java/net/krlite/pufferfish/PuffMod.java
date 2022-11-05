package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.event.InputEventHandler;
import net.krlite.pufferfish.render.ColoredRenderer;
import net.krlite.pufferfish.render.ColoredTextureRenderer;
import net.krlite.pufferfish.render.MaskedTextureRenderer;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.krlite.pufferfish.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuffMod implements ClientModInitializer {
	public static final String MOD_ID = "puff";
	public static final Logger LOGGER = LoggerFactory.getLogger("Pufferfish");
	public static final ColoredTextureRenderer CTR = new ColoredTextureRenderer();
	public static final MaskedTextureRenderer MTR = new MaskedTextureRenderer();
	public static final ColoredRenderer CR = new ColoredRenderer();

	@Override
	public void onInitializeClient() {
		PuffConfigs.init();
		AxisLocker.init();
		InputEventHandler.init();
		ChatUtil.init();
		ScreenshotFlashRenderer.init();
	}
}
