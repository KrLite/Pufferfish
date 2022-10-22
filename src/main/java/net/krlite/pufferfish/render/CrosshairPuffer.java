package net.krlite.pufferfish.render;

import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CrosshairPuffer extends DrawableHelper {
    private static int scaledWidth;
    private static int scaledHeight;

    public static double crosshairScaleTarget = PuffConfigs.crosshairSize;
    public static double crosshairScale = crosshairScaleTarget;

    public static void puffCrosshair(MatrixStack matrixStack) {
        puffCrosshair(matrixStack, 1);
    }

    public static void puffCrosshair(MatrixStack matrixStack, float multiplier) {
        matrixStack.translate(
                (scaledWidth * -0.5F) * (crosshairScale - 1) * multiplier,
                (scaledHeight * -0.5F) * (crosshairScale - 1) * multiplier,
                0.0
        );
        matrixStack.scale(
                (float) crosshairScale * multiplier,
                (float) crosshairScale * multiplier,
                (float) crosshairScale * multiplier
        );
    }

    private static void lerp() {
        crosshairScale = MathHelper.lerp(0.65F, crosshairScale, crosshairScaleTarget);
    }

    public static void update(int scaledWidth, int scaledHeight) {
        CrosshairPuffer.scaledWidth = scaledWidth;
        CrosshairPuffer.scaledHeight = scaledHeight;
        lerp();
    }
}
