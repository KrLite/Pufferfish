package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.interaction_map.render.AnchorRenderer;
import net.krlite.pufferfish.interaction_map.util.AnchorProvider;
import net.krlite.pufferfish.interaction_map.util.solver.AnchorSolver;
import net.krlite.pufferfish.render.CrosshairPuffer;
import net.krlite.pufferfish.util.AxisLocker;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Optional;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper{
    // Access Scaled Width & Height
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    // Update
    @Inject(method = "render", at = @At("HEAD"))
    private void head(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        AxisLocker.update(MinecraftClient.getInstance().player);
        CrosshairPuffer.update(scaledWidth, scaledHeight);
    }

    // Set Crosshair Render Style
    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private void setCrosshairStyle(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        switch ( PuffConfigs.corsshairStyle ) {
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

        // Render Anchor
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Optional<Double> anchorPos = AnchorProvider.lastDeathPosition(player, 0.95F);
        Optional<Double> anchorDistance = AnchorProvider.lastDeathDistance(player);

        if ( anchorPos.isPresent() && anchorDistance.isPresent() ) {
            float opacity =
                    (float) MathHelper.clamp(
                            anchorDistance.get() >= 5
                                    ? (128 / anchorDistance.get())
                                    : Math.pow(anchorDistance.get() / 5, 1.7),
                            0, 1
                    );

            // Anchor
            AnchorRenderer.render(
                    matrixStack, ColorUtil.castAlpha(Color.RED, opacity),
                    (float) (double) anchorPos.get(), 540 * opacity
            );
        }
    }
}
