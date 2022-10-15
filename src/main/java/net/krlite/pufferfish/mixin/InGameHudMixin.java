package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.AxisLocker;
import net.krlite.pufferfish.util.CrosshairPuffer;
import net.krlite.pufferfish.util.ScreenEdgeOverlay;
import net.krlite.pufferfish.util.ScreenshotFlasher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper{
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    @Shadow protected abstract void renderOverlay(Identifier texture, float opacity);

    @Shadow public abstract TextRenderer getTextRenderer();

    // Update
    @Inject(method = "render", at = @At("HEAD"))
    private void update(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        AxisLocker.update(MinecraftClient.getInstance().player);
    }

    // Relocate Crosshair
    @Redirect(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ), slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F")
            )
    )
    private void drawCrosshair(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrixStack, PuffConfigs.lerpDelta == 0.0 ? x : 0, PuffConfigs.lerpDelta == 0.0 ? y : 0, u, v, width, height);
        CrosshairPuffer.set(width, height);
    }

    // Relocate Attack Indicator
    @Redirect(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ), slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isAlive()Z")
            )
    )
    private void drawCrosshairAttackIndicator(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrixStack, PuffConfigs.lerpDelta == 0.0 ? x : 0, PuffConfigs.lerpDelta == 0.0 ? y : 16, u, v, width, height);
    }

    // Puff Crosshair
    @Inject(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.BY
            ), cancellable = true
    )
    private void scaleCrosshairBefore(MatrixStack matrixStack, CallbackInfo ci) {
        if ( PuffConfigs.lerpDelta == 0.0 ) ci.cancel();
        CrosshairPuffer.puffCrosshair(matrixStack, scaledWidth, scaledHeight);
    }

    // Pop Matrix Stack
    @Inject(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.AFTER
            ), cancellable = true
    )
    private void scaleCrosshairAfter(MatrixStack matrixStack, CallbackInfo ci) {
        if ( PuffConfigs.lerpDelta == 0.0 ) ci.cancel();
        matrixStack.pop();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        RenderSystem.enableBlend();

        // Render Screen Edge Overlay by RGBA
        renderOverlayByRGBA(
                ScreenEdgeOverlay.R, ScreenEdgeOverlay.G, ScreenEdgeOverlay.B, ScreenEdgeOverlay.BK,
                ScreenEdgeOverlay.red, ScreenEdgeOverlay.green, ScreenEdgeOverlay.blue, ScreenEdgeOverlay.alpha
        );

        // Render Flash
        if ( ScreenshotFlasher.flashOpacity > 0 ) {
            this.renderOverlay(ScreenshotFlasher.FLASH, ScreenshotFlasher.flashOpacity);
        }
    }

    // Render Axis Hint
    @Inject(method = "render", at = @At("HEAD"))
    private void renderAxisHint(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        Text hint =
                AxisLocker.axisLock.get(AxisLocker.Axis.PITCH)
                        ? AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                ? new LiteralText("Pitch & Yaw")
                                : new LiteralText("Pitch")
                        : AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                ? new LiteralText("Yaw")
                                : null;

        float r = ScreenEdgeOverlay.red;
        float g = ScreenEdgeOverlay.green;
        float b = ScreenEdgeOverlay.blue;
        float a = ScreenEdgeOverlay.alpha;

        matrixStack.push();
        float f = a / ScreenEdgeOverlay.targetColor.getColor(3);
        if ( hint != null ) {
            matrixStack.translate(
                    scaledWidth / 2.0F - this.getTextRenderer().getWidth(hint) / 2.0F + 0.5F,
                    scaledHeight / 2.0F + 7 + f * 5,
                    0.0F
            );

            this.getTextRenderer().draw(
                    matrixStack,
                    hint,
                    0,
                    0,
                    (MathHelper.clamp(Math.round(a * 255.0F) * 3, 0, 255) << 24)
                            | (Math.round(r) << 16)
                            | (Math.round(g) << 8)
                            | (Math.round(b))
            );
        }
        matrixStack.scale(f, f, f);
        matrixStack.pop();
    }

    // Render Overlay Dynamically through RGBA Values
    private void renderOverlayByRGBA(
            Identifier R, Identifier G, Identifier B, Identifier BK,
            float red, float green, float blue, float alpha
    ) {
        // Black
        RenderSystem.colorMask(true, true, true, true);
        renderOverlay(
                BK,
                alpha
        );

        // Red
        RenderSystem.colorMask(true, false, false, true);
        renderOverlay(
                R,
                (red / 255) * alpha
        );

        // Green
        RenderSystem.colorMask(false, true, false, true);
        renderOverlay(
                G,
                (green / 255) * alpha
        );

        // Blue
        RenderSystem.colorMask(false, false, true, true);
        renderOverlay(
                B,
                (blue / 255) * alpha
        );

        // Reset Color Mask
        RenderSystem.colorMask(true, true, true, true);

        /*
        // Log
        PuffMod.LOGGER.warn(
                        r + " | " +
                        g + " | " +
                        b + " / " +
                        alpha
        );
         */
    }
}
