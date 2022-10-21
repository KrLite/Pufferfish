package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.*;
import net.krlite.pufferfish.util.AxisLocker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper{
    // Access Scaled Width & Height
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    private static Color cameraOverlay = PuffConfigs.TRANSLUCENT;

    // Update
    @Inject(method = "render", at = @At("HEAD"))
    private void update(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        AxisLocker.update(MinecraftClient.getInstance().player);
    }

    // Set Crosshair Render Style
    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private void setCrosshairStyle(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.DST_COLOR, GlStateManager.DstFactor.DST_COLOR,
                srcAlpha, dstAlpha
        );
    }

    // Puff Crosshair & Push Matrix Stack
    @Inject(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void scaleCrosshairBefore(MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.push();
        CrosshairPuffer.puffCrosshair(matrixStack);
    }

    // Pop Matrix Stack
    @Inject(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.AFTER
            )
    )
    private void scaleCrosshairAfter(MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.pop();
    }

    // Render
    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        RenderSystem.enableBlend();

        // Render Camera Overlay
        cameraOverlay = CameraOverlayHandler.lerpColor(
                cameraOverlay,
                AxisLocker.axisLock.get(AxisLocker.Axis.PITCH)
                        ? AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                ? new Color(
                                        (PuffConfigs.ocean.getRed() + PuffConfigs.scarlet.getRed()) / 2,
                                        (PuffConfigs.ocean.getGreen() + PuffConfigs.scarlet.getGreen()) / 2,
                                        (PuffConfigs.ocean.getBlue() + PuffConfigs.scarlet.getBlue()) / 2,
                                        255
                                )
                                : PuffConfigs.ocean
                        : AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                ? PuffConfigs.scarlet
                                : PuffConfigs.TRANSLUCENT,
                cameraOverlay.getAlpha() / 255.0F,
                (AxisLocker.axisLock.get(AxisLocker.Axis.PITCH) || AxisLocker.axisLock.get(AxisLocker.Axis.YAW))
                        ? 0.27F : 0
        );
        ColoredTextureRenderer.renderColoredOverlay(CameraOverlayHandler.BASIC, cameraOverlay);

        // Render Flash
        if ( ScreenshotFlasher.flashOpacity > 0 ) {
            ColoredTextureRenderer.renderColoredOverlay(
                    ScreenshotFlasher.FLASH,
                    new Color(255, 255, 255, (int) (ScreenshotFlasher.flashOpacity * 255))
            );
        }
    }

    // Render Axis Hint
    @Inject(method = "render", at = @At("HEAD"))
    private void renderAxisHint(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        AxisHintHandler.draw(matrixStack, scaledWidth, scaledHeight);
    }
}
