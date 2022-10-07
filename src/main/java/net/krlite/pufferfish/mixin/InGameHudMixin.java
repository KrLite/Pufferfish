package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.pufferfish.PuffMod.crosshairScale;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper{
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    private int crosshairWidth;
    private int crosshairHeight;

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
        if ( PuffConfigs.lerpDelta == 0.0 ) {
            drawTexture(matrixStack, x, y, u, v, width, height);
            return;
        }

        crosshairWidth = width;
        crosshairHeight = height;
        drawTexture(matrixStack, 0, 0, u, v, width, height);
    }

    @Redirect(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ), slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isAlive()Z")
            )
    )
    private void drawCrosshairAttackIndicator(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        if ( PuffConfigs.lerpDelta == 0.0 ) {
            drawTexture(matrixStack, x, y, u, v, width, height);
            return;
        }

        drawTexture(matrixStack, 0, 16, u, v, width, height);
    }

    @Inject(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.BY
            ), cancellable = true
    )
    private void scaleCrosshairBefore(MatrixStack matrices, CallbackInfo ci) {
        if ( PuffConfigs.lerpDelta == 0.0 ) {
            ci.cancel();
        }

        crosshairScale = (float) MathHelper.lerp(1.0 / PuffConfigs.lerpDelta, crosshairScale, 1.0);
        matrices.push();
        matrices.translate(this.scaledWidth / 2.0F - (crosshairWidth * 0.5F) * crosshairScale,
                this.scaledHeight / 2.0F - (crosshairHeight * 0.5F) * crosshairScale,
                0.0);
        matrices.scale(crosshairScale, crosshairScale, crosshairScale);
    }

    @Inject(method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.AFTER
            ), cancellable = true
    )
    private void scaleCrosshairAfter(MatrixStack matrices, CallbackInfo ci) {
        if ( PuffConfigs.lerpDelta == 0.0 ) {
            ci.cancel();
        }

        matrices.pop();
    }
}
