package net.krlite.pufferfish.mixin.handler.screen_background;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.util.ScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenBackgroundHandler {
    @Shadow @Nullable protected MinecraftClient client;

    @Redirect(
            method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;fillGradient(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void renderBackground(Screen screen, MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int colorBegin, int colorEnd) {
        ScreenUtil.show(true);
    }

    @Inject(
            method = "renderBackgroundTexture",
            at = @At("TAIL")
    )
    private void renderBackgroundTexture(int vOffset, CallbackInfo ci) {
        if ( client != null ) {
            RenderSystem.enableBlend();
            Screen screen = client.currentScreen;

            if (
                    screen instanceof LevelLoadingScreen
                            || screen instanceof ProgressScreen
            ) {
                ScreenUtil.updateLoading(0.107, true);
                ScreenUtil.renderBackground();
            }

            if (
                    screen instanceof MessageScreen
                            || screen instanceof DownloadingTerrainScreen
            ) {
                ScreenUtil.updateLoading(0.23, false);
                ScreenUtil.renderBackground();
            }
        }
    }
}
