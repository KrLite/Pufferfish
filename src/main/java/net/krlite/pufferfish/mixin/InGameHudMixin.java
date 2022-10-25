package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.*;
import net.krlite.pufferfish.util.AxisLocker;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.Default;
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
import java.util.Objects;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper{
    // Access Scaled Width & Height
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    private static Color cameraOverlay = ColorUtil.TRANSLUCENT;

    // Update
    @Inject(method = "render", at = @At("HEAD"))
    private void update(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        AxisLocker.update(MinecraftClient.getInstance().player);
        CrosshairPuffer.update(scaledWidth, scaledHeight);
    }

    // Set Crosshair Render Style
    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private void setCrosshairStyle(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        switch (PuffConfigs.corsshairStyle) {
            case PUFFERFISH -> RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.DST_COLOR, GlStateManager.DstFactor.DST_COLOR,
                    srcAlpha, dstAlpha
            );

            case OPAQUE -> RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO,
                    srcAlpha, dstAlpha
            );

            default -> RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcAlpha, dstAlpha);
        }
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
                                        (ColorUtil.pitchColor.getRed() + ColorUtil.yawColor.getRed()) / 2,
                                        (ColorUtil.pitchColor.getGreen() + ColorUtil.yawColor.getGreen()) / 2,
                                        (ColorUtil.pitchColor.getBlue() + ColorUtil.yawColor.getBlue()) / 2,
                                        255
                                )
                                : ColorUtil.pitchColor
                        : AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                ? ColorUtil.yawColor
                                : ColorUtil.TRANSLUCENT,
                cameraOverlay.getAlpha() / 255.0F,
                (AxisLocker.axisLock.get(AxisLocker.Axis.PITCH) || AxisLocker.axisLock.get(AxisLocker.Axis.YAW))
                        ? 0.27F : 0
        );
        ColoredTextureRenderer.renderColoredOverlay(CameraOverlayHandler.BASIC, cameraOverlay);

        // Render Flash
        if ( ScreenshotFlashRenderer.flashOpacity > 0 ) {
            ScreenshotFlashRenderer.renderScreenshotFlash();
        }
    }

    // Render Axis Hint
    @Inject(method = "render", at = @At("HEAD"))
    private void renderAxisHint(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        AxisHintHandler.draw(matrixStack, scaledWidth, scaledHeight);
    }
}
