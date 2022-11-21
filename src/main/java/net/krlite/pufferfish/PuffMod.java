package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.event.InputEventHandler;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.krlite.pufferfish.render.renderer.PuffProxiedRenderer;
import net.krlite.pufferfish.util.AxisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuffMod implements ClientModInitializer {
	public static final String MOD_ID = "puff";
	public static final Logger LOGGER = LoggerFactory.getLogger("Pufferfish");

	@Override
	public void onInitializeClient() {
		PuffConfigs.init();
		AxisUtil.init();
		InputEventHandler.init();
		ScreenshotFlashRenderer.init();

		// Renderer
		PuffRenderer.init();
		PuffProxiedRenderer.init();

		// TODO: Clean Utils, Textures and Mixins
		// TODO: Deprecate @Redirections
		// TODO: Restructure Renderers
	}
}
