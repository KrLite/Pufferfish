package net.krlite.pufferfish.render.extra;

import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.math.PreciseColor;
import net.krlite.pufferfish.render.CameraOverlayHandler;
import net.krlite.pufferfish.render.PuffDelayedRenderer;
import net.krlite.pufferfish.util.AxisUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ExtraBeforeRenderer {
    private static final PreciseColor axisColor = PreciseColor.empty();

    public static void render(MatrixStack matrixStack, float tickDelta, long startTime, boolean tick) {
        MinecraftClient client = MinecraftClient.getInstance();
        int
                width = client.getWindow().getScaledWidth(),
                height = client.getWindow().getScaledHeight();

        // Render Camera Overlay
        axisColor.blend(
                AxisUtil.axisLock.get(AxisUtil.Axis.PITCH)
                        ? AxisUtil.axisLock.get(AxisUtil.Axis.YAW)
                        ? PreciseColor.of(ColorUtil.blendColor(AxisUtil.pitchColor, AxisUtil.yawColor))
                        : PreciseColor.of(AxisUtil.pitchColor)
                        : AxisUtil.axisLock.get(AxisUtil.Axis.YAW)
                        ? PreciseColor.of(AxisUtil.yawColor)
                        : PreciseColor.empty(),
                0.0147
        );
        CameraOverlayHandler.renderCameraOverlay(axisColor.independent().multipleAlpha(0.373).get(), matrixStack);

        // Render Delayed Subtitles Hud
        if ( PuffConfigs.enableChatAnimation ) {
            PuffDelayedRenderer.SUBTITLES_HUD.render(matrixStack);
        }
    }
}
