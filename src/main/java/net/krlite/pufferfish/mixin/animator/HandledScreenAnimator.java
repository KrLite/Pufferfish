package net.krlite.pufferfish.mixin.animator;

import net.krlite.pufferfish.util.ScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenAnimator {
    private void scale(MatrixStack matrixStack) {
        float scale = (float) (1 - ScreenUtil.opacity) * 0.051F;

        matrixStack.translate(
                -MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 * scale,
                -MinecraftClient.getInstance().getWindow().getScaledHeight() / 2.0 * scale,
                0
        );
        matrixStack.scale(scale + 1, scale + 1, scale + 1);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawBackground(Lnet/minecraft/client/util/math/MatrixStack;FII)V"
            )
    )
    private void pushBackgroundMatrixStack(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.push();
        scale(matrixStack);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawBackground(Lnet/minecraft/client/util/math/MatrixStack;FII)V",
                    shift = At.Shift.AFTER
            )
    )
    private void popBackgroundMatrixStack(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/util/math/MatrixStack;II)V"
            )
    )
    private void pushForegroundMatrixStack(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.push();
        scale(matrixStack);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/util/math/MatrixStack;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void popForegroundMatrixStack(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.pop();
    }
}
