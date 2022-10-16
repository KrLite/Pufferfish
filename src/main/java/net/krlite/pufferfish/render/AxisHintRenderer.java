package net.krlite.pufferfish.render;

import net.krlite.pufferfish.PuffKeys;
import net.krlite.pufferfish.util.AxisLocker;
import net.krlite.pufferfish.util.PuffIdentifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

public class AxisHintRenderer extends DrawableHelper {
    public static Text axisHint = null;
    private static final float delta = 0.25F;
    private static float red = 0.0F, green = 0.0F, blue = 0.0F;
    public static float hintAlpha = 0;

    public static void updateHint() {
        axisHint =
                (AxisLocker.axisLock.get(AxisLocker.Axis.PITCH) || AxisLocker.axisPing.get(AxisLocker.Axis.PITCH))
                        ? (AxisLocker.axisLock.get(AxisLocker.Axis.YAW) || AxisLocker.axisPing.get(AxisLocker.Axis.YAW))
                                ? new TranslatableText(PuffIdentifier.translationKey("hint", "pitch_yaw"))
                                : new TranslatableText(PuffIdentifier.translationKey("hint", "pitch"))
                        : (AxisLocker.axisLock.get(AxisLocker.Axis.YAW) || AxisLocker.axisPing.get(AxisLocker.Axis.YAW))
                                ? new TranslatableText(PuffIdentifier.translationKey("hint", "yaw"))
                                : axisHint;
    }

    public static void draw(MatrixStack matrixStack, int scaledWidth, int scaledHeight) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        matrixStack.push();
        if ( axisHint != null && hintAlpha > 0.1 ) {
            int hintWidth = textRenderer.getWidth(axisHint);

            CrosshairPuffer.puffCrosshair(matrixStack, scaledWidth, scaledHeight + 34, hintWidth, - hintAlpha * 2.5F, hintAlpha);

            textRenderer.draw(
                    matrixStack,
                    axisHint,
                    0,
                    0,
                    (Math.round(hintAlpha * 255.0F) << 24)
                            | (Math.round(red) << 16)
                            | (Math.round(green) << 8)
                            | (Math.round(blue))
            );
        }
        matrixStack.pop();
    }

    private static void lerp() {
        hintAlpha =
                MathHelper.lerp(
                        delta, hintAlpha,
                        (
                                (AxisLocker.axisLock.get(AxisLocker.Axis.PITCH) || AxisLocker.axisLock.get(AxisLocker.Axis.YAW))
                                        || (
                                                (!AxisLocker.flippingAxisPitch && !AxisLocker.flippingAxisYaw)
                                                        && (AxisLocker.axisPing.get(AxisLocker.Axis.PITCH) || AxisLocker.axisPing.get(AxisLocker.Axis.YAW))
                                )
                        )
                                ? 1.0F
                                : 0
                );
    }

    public static void update() {
        lerp();

        if ( ScreenEdgeOverlayRenderer.targetColor != ScreenEdgeOverlayRenderer.Color.NONE ) {
            red = ScreenEdgeOverlayRenderer.targetColor.getColor(1);
            green = ScreenEdgeOverlayRenderer.targetColor.getColor(2);
            blue = ScreenEdgeOverlayRenderer.targetColor.getColor(3);
        }
    }
}
