package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.CrosshairPuffer;
import net.krlite.pufferfish.util.ScreenshotFlasher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
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

    private static final Identifier FLASH = new Identifier("pufferfish", "textures/misc/flash.png");

    // Relocate crosshair
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

    // Relocate attack indicator
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

    // Puff crosshair
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

    // Pop matrix stack
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
        if ( ScreenshotFlasher.flashOpacity > 0 ) {
            this.renderOverlay(FLASH, ScreenshotFlasher.flashOpacity);
        }
    }
}
