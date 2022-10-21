package net.krlite.pufferfish.render;

import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CrosshairPuffer extends DrawableHelper {
    public static double crosshairScaleTarget = PuffConfigs.crosshairSize;
    public static double crosshairScale = crosshairScaleTarget;

    public static void puffCrosshair(MatrixStack matrixStack) {
        puffCrosshair(matrixStack, 1);
    }

    public static void puffCrosshair(MatrixStack matrixStack, float multiplier) {
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int height = MinecraftClient.getInstance().getWindow().getScaledHeight();

        matrixStack.translate(
                (width * -0.5F) * (crosshairScale - 1) * multiplier,
                (height * -0.5F) * (crosshairScale - 1) * multiplier,
                0.0
        );
        matrixStack.scale(
                (float) crosshairScale * multiplier,
                (float) crosshairScale * multiplier,
                (float) crosshairScale * multiplier
        );
    }

    private static void lerp() {
        crosshairScale = MathHelper.lerp(0.45F, crosshairScale, crosshairScaleTarget);
    }

    public static void update() {
        lerp();
    }
}
