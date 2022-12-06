package net.krlite.pufferfish;

import net.fabricmc.api.ClientModInitializer;
import net.krlite.equator.util.IdentifierBuilder;
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
	public static final IdentifierBuilder identifierBuilder = new IdentifierBuilder(MOD_ID);

	@Override
	public void onInitializeClient() {
		AxisUtil.init();
		InputEventHandler.init();
		ScreenshotFlashRenderer.init();

		// Renderer
		PuffRenderer.init();
		PuffProxiedRenderer.init();
	}
}
