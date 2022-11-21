package net.krlite.pufferfish.mixin.handler;

import com.mojang.logging.LogUtils;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotHandler {
    @Final @Shadow private static Logger LOGGER = LogUtils.getLogger();

    @Shadow private static File getScreenshotFilename(File directory) {
        return null;
    }

    @Shadow public static NativeImage takeScreenshot(Framebuffer framebuffer) {
        return null;
    }

    @Inject(method = "takeScreenshot", at = @At("TAIL"))
    private static void ScreenshotHandler$takeScreenshot(Framebuffer framebuffer, CallbackInfoReturnable<NativeImage> cir) {
        ScreenshotFlashRenderer.setOpacity(1.0F);
    }

    @Inject(method = "saveScreenshotInner", at = @At("HEAD"), cancellable = true)
    private static void ScreenshotHandler$saveScreenshotInner(File gameDirectory, String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
        // TODO: Deprecate This
        NativeImage nativeImage = takeScreenshot(framebuffer);
        File fileDummy = new File(gameDirectory, "screenshots");
        fileDummy.mkdir();
        File file;
        if (fileName == null) {
            file = getScreenshotFilename(fileDummy);
        } else {
            file = new File(fileDummy, fileName);
        }

        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(file);
                messageReceiver.accept(
                        Text.literal("[")
                                .formatted(Formatting.GRAY, Formatting.ITALIC)
                                .append(
                                        Text.translatable(
                                                "screenshot.success",
                                                Text.literal(file.getName())
                                                        .formatted(Formatting.GRAY, Formatting.ITALIC, Formatting.UNDERLINE)
                                                        .styled(
                                                                (style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath()))
                                                        )
                                        )
                                ).append(
                                        Text.literal("]")
                                                .formatted(Formatting.GRAY, Formatting.ITALIC)
                                )
                );
            } catch (Exception exception) {
                LOGGER.warn("Couldn't save screenshot", exception);
                messageReceiver.accept(
                        Text.literal("[")
                                .formatted(Formatting.DARK_RED, Formatting.ITALIC)
                                .append(
                                        Text.translatable("screenshot.failure", exception.getMessage())
                                                .formatted(Formatting.DARK_RED, Formatting.ITALIC)
                                ).append(
                                        Text.literal("]")
                                                .formatted(Formatting.DARK_RED, Formatting.ITALIC)
                                )
                );
            } finally {
                nativeImage.close();
            }

        });

        ci.cancel();
    }
}
