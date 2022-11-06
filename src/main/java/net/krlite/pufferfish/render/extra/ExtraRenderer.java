package net.krlite.pufferfish.render.extra;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.render.PuffDelayedRenderer;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ExtraRenderer {
    public static void render(MatrixStack matrixStack) {
        if ( matrixStack != null ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            MinecraftClient client = MinecraftClient.getInstance();
            int
                    width = client.getWindow().getScaledWidth(),
                    height = client.getWindow().getScaledHeight();

            // Render Flash
            ScreenshotFlashRenderer.renderScreenshotFlash();

            // Render Delayed Chat Screen
            PuffDelayedRenderer.CHAT_SCREEN.render(matrixStack);

            RenderSystem.disableBlend();
        }
    }
}
