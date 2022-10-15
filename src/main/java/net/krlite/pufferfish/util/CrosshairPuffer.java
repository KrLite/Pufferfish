package net.krlite.pufferfish.util;

import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CrosshairPuffer extends DrawableHelper {
    public static double crosshairScale = PuffConfigs.crosshairOriginal;
    public static double crosshairScaleTarget = crosshairScale;
    private static int width;
    private static int height;

    public static void set(int crosshairWidth, int crosshairHeight) {
        width = crosshairWidth;
        height = crosshairHeight;
    }

    public static void puffCrosshair(MatrixStack matrixStack, int scaledWidth, int scaledHeight) {
        crosshairScale = MathHelper.lerp(1.0 / PuffConfigs.lerpDelta, crosshairScale, crosshairScaleTarget);
        matrixStack.push();
        matrixStack.translate(scaledWidth / 2.0F - (width * 0.5F) * crosshairScale,
                scaledHeight / 2.0F - (height * 0.5F) * crosshairScale,
                0.0);
        matrixStack.scale((float) crosshairScale, (float) crosshairScale, (float) crosshairScale);
    }
}