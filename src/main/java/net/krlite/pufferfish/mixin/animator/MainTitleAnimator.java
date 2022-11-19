package net.krlite.pufferfish.mixin.animator;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.math.solver.EasingFunctions;
import net.krlite.pufferfish.util.TitleUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MainTitleAnimator {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;MINECRAFT_TITLE_TEXTURE:Lnet/minecraft/util/Identifier;")
            )
    )
    private void setShaderColor(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
        if ( alpha > 0 && TitleUtil.titleMeasuringStartMs == -1 && PuffConfigs.enableTitleAnimation ) {
            TitleUtil.titleMeasuringStartMs = Util.getMeasuringTimeMs();
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;MINECRAFT_TITLE_TEXTURE:Lnet/minecraft/util/Identifier;",
                    shift = At.Shift.AFTER
            )
    )
    private void update(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        double
                time = Util.getMeasuringTimeMs() - TitleUtil.titleMeasuringStartMs,
                offset = PuffConfigs.enableTitleAnimation
                        ? MinecraftClient.getInstance().getWindow().getScaledHeight() / 3.5
                        : 0;
        if ( time > TitleUtil.titleMs ) time = TitleUtil.titleMs;
        TitleUtil.titlePos = TitleUtil.titleMeasuringStartMs == -1
                ? -offset
                : EasingFunctions.easeOutBounce(time, -offset, offset, TitleUtil.titleMs);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeMinecraft(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.push();
        matrixStack.translate(0, TitleUtil.titlePos, 0);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterMinecraft(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeEdition(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.push();
        matrixStack.translate(0, TitleUtil.titlePos, 0);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void afterEdition(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen$DeprecationNotice;render(Lnet/minecraft/client/util/math/MatrixStack;I)V")
            )
    )
    private void splash(MatrixStack matrixStack, double x, double y, double z) {
        matrixStack.translate(x, TitleUtil.titlePos + 70, z);
    }
}
