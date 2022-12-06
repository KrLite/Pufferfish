package net.krlite.pufferfish.mixin.animator;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfig;
import net.krlite.pufferfish.math.IdentifierSprite;
import net.krlite.pufferfish.math.PreciseColor;
import net.krlite.pufferfish.render.renderer.CrosshairPuffer;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.AxisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class CrosshairAnimator extends DrawableHelper{
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    private static double opacity = 1;

    // Update
    @Inject(method = "render", at = @At("HEAD"))
    private void head(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        AxisUtil.update(MinecraftClient.getInstance().player);
        CrosshairPuffer.update(scaledWidth, scaledHeight);
    }

    // Set Crosshair Render Style
    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private void setCrosshairStyle(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        double multiplier = 1 - 0.53 * (MathHelper.abs(MathHelper.sin((float) ((Util.getMeasuringTimeMs() % 10500L) / 10500.0 * (Math.PI * 2)))));

        switch ( PuffConfig.CROSSHAIR_RENDER_STYLE.getValue() ) {
            case PUFFERFISH -> {
                RenderSystem.blendFunc(
                        GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.DST_COLOR
                );
                opacity = 0.625 * multiplier;
            }

            case OPAQUE -> {
                RenderSystem.defaultBlendFunc();
                opacity = 1;
            }

            default -> {
                RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcAlpha, dstAlpha);
                opacity = 1;
            }
        }
    }

    // Puff Crosshair & Push Matrix Stack
    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void scaleCrosshairBefore(MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.push();
        CrosshairPuffer.puffCrosshair(matrixStack);
    }

    // Re-render Crosshair
    @Redirect(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;")
            )
    )
    private void renderCrosshair(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        matrixStack.push();
        CrosshairPuffer.puffCrosshair(matrixStack);

        switch ( PuffConfig.CROSSHAIR_STYLE.getValue() ) {
            case EMPTY -> {}

            case VANILLA -> PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                    CrosshairPuffer.VANILLA_CROSSHAIR,
                    new PreciseColor(Color.WHITE).multipleAlpha(opacity).get(),
                    matrixStack,
                    (scaledWidth - 15) / 2.0F, (scaledHeight - 15) / 2.0F,
                    (scaledWidth + 15) / 2.0F, (scaledHeight + 15) / 2.0F
            );

            default -> {
                IdentifierSprite STYLE = CrosshairPuffer.CROSSHAIR.get(PuffConfig.CROSSHAIR_STYLE.getValue().getIndex());

                PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                        STYLE,
                        new PreciseColor(Color.WHITE).multipleAlpha(opacity).get(),
                        matrixStack,
                        (scaledWidth - 15) / 2.0F, (scaledHeight - 15) / 2.0F,
                        (scaledWidth + 15) / 2.0F, (scaledHeight + 15) / 2.0F
                );
            }
        }

        RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        matrixStack.pop();
    }

    // Re-render Attack Indicator
    @Redirect(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;")
            )
    )
    private void renderAttackIndicator(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        PuffRenderer.COLORED_TEXTURE.renderPositionedColoredTexture(
                GUI_ICONS_TEXTURE,
                PuffConfig.HOTBAR_POSITION.getValue().isLeft()
                        ? new PreciseColor(Color.WHITE).castAlpha(opacity).get()
                        : Color.WHITE,
                matrixStack,
                x, y, u, v, width, height
        );
    }

    // Pop Matrix Stack
    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.AFTER
            )
    )
    private void scaleCrosshairAfter(MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.pop();
    }
}
