package net.krlite.pufferfish.render.extra;

import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.math.PreciseColor;
import net.krlite.pufferfish.render.CameraOverlayHandler;
import net.krlite.pufferfish.render.PuffDelayedRenderer;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.krlite.pufferfish.util.AxisUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ExtraAfterRenderer {
    public static void render(MatrixStack matrixStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        int
                width = client.getWindow().getScaledWidth(),
                height = client.getWindow().getScaledHeight();

        // Render Flash
        ScreenshotFlashRenderer.renderScreenshotFlash();

        // Render Delayed Chat Screen
        if ( PuffConfigs.enableChatAnimation ) {
            PuffDelayedRenderer.CHAT_SCREEN.render(matrixStack);
        }
    }
}
