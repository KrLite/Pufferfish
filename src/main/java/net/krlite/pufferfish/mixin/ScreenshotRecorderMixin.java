package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
    @Inject(method = "takeScreenshot", at = @At("TAIL"))
    private static void takeScreenshot(Framebuffer framebuffer, CallbackInfoReturnable<NativeImage> cir) {
        ScreenshotFlashRenderer.setOpacity(1.0F);
    }
}
