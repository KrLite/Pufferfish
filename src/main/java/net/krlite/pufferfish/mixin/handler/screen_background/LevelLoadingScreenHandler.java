package net.krlite.pufferfish.mixin.handler.screen_background;

import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.ScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenHandler {
    @Shadow @Final private WorldGenerationProgressTracker progressProvider;

    @Inject(
            method = "removed",
            at = @At("TAIL")
    )
    private void removed(CallbackInfo ci) {
        ScreenUtil.setOpacity(1);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"
            )
    )
    private void drawProgressBar(MatrixStack matrixStack, WorldGenerationProgressTracker progressProvider, int xCentered, int yCentered, int pixelSize, int pixelMargin) {
        PuffRenderer.COLORED.fillColored(
                new MatrixStack(),
                0,
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 32.0F,
                MinecraftClient.getInstance().getWindow().getScaledWidth() * progressProvider.getProgressPercentage() / 100.0F,
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 31.3F,
                Color.WHITE
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"
            )
    )
    private void cancelDrawProgress(MatrixStack matrixStack, TextRenderer textRenderer, String text, int xCentered, int y, int color) {
        LevelLoadingScreen.drawCenteredText(
                matrixStack, textRenderer, text,
                (MinecraftClient.getInstance().getWindow().getScaledWidth() - 28) * progressProvider.getProgressPercentage() / 100 - textRenderer.getWidth("100%") / 2,
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 28,
                color
        );
    }
}
