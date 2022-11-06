package net.krlite.pufferfish.render.extra;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.render.CameraOverlayHandler;
import net.krlite.pufferfish.render.ScreenshotFlashRenderer;
import net.krlite.pufferfish.util.AxisLocker;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ExtraInGameHudRenderer {
    private static Color axisColor = ColorUtil.TRANSLUCENT;

    public static void render(MatrixStack matrixStack) {
        if ( matrixStack != null ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            MinecraftClient client = MinecraftClient.getInstance();
            int
                    width = client.getWindow().getScaledWidth(),
                    height = client.getWindow().getScaledHeight();

            // Render Camera Overlay
            axisColor = ColorUtil.lerpColor(
                    axisColor,
                    AxisLocker.axisLock.get(AxisLocker.Axis.PITCH)
                            ? AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                    ? ColorUtil.blendColor(ColorUtil.pitchColor, ColorUtil.yawColor)
                                    : ColorUtil.pitchColor
                            : AxisLocker.axisLock.get(AxisLocker.Axis.YAW)
                                    ? ColorUtil.yawColor
                                    : ColorUtil.TRANSLUCENT,
                    axisColor.getAlpha() / 255.0F,
                    (AxisLocker.axisLock.get(AxisLocker.Axis.PITCH) || AxisLocker.axisLock.get(AxisLocker.Axis.YAW))
                            ? 0.27F : 0,
                    0.0332F
            );
            CameraOverlayHandler.renderCameraOverlay(axisColor);

            RenderSystem.disableBlend();
        }
    }
}
