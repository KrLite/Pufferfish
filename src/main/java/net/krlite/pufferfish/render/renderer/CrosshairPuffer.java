package net.krlite.pufferfish.render.renderer;

import net.krlite.equator.core.sprite.HorizontalSprite;
import net.krlite.equator.core.sprite.IdentifierSprite;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfig;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CrosshairPuffer extends DrawableHelper {
    private static int scaledWidth;
    private static int scaledHeight;

    public static double crosshairScaleTarget = PuffConfig.CROSSHAIR_SIZE.getValue();
    public static double crosshairScale = crosshairScaleTarget;

    public static final IdentifierSprite VANILLA_CROSSHAIR = new IdentifierSprite(GUI_ICONS_TEXTURE, 256, 0, 0, 15, 15);
    public static final HorizontalSprite CROSSHAIR = new HorizontalSprite(PuffMod.identifierBuilder.texture("gui", "crosshair"), 16);

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
        crosshairScale = MathHelper.lerp(0.4, crosshairScale, crosshairScaleTarget);
    }

    public static void update(int scaledWidth, int scaledHeight) {
        CrosshairPuffer.scaledWidth     = scaledWidth;
        CrosshairPuffer.scaledHeight    = scaledHeight;
        lerp();
    }
}
