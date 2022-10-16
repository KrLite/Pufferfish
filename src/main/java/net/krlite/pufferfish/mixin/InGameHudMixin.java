package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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
        matrixStack.push();
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
        AxisHintProvider.draw(matrixStack, scaledWidth, scaledHeight);
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
