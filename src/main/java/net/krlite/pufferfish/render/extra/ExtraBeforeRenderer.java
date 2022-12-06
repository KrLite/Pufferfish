package net.krlite.pufferfish.render.extra;

import net.krlite.equator.core.PreciseColor;
import net.krlite.pufferfish.config.PuffConfig;
import net.krlite.pufferfish.render.renderer.CameraOverlayRenderer;
import net.krlite.pufferfish.render.renderer.PuffProxiedRenderer;
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
                        ? new PreciseColor(ColorUtil.blendColor(PuffConfig.PITCH_COLOR.getValue(), PuffConfig.YAW_COLOR.getValue()))
                        : new PreciseColor(PuffConfig.PITCH_COLOR.getValue())
                        : AxisUtil.axisLock.get(AxisUtil.Axis.YAW)
                        ? new PreciseColor(PuffConfig.YAW_COLOR.getValue())
                        : PreciseColor.empty(),
                0.0147
        );
        CameraOverlayRenderer.renderCameraOverlay(axisColor.independent().multipleAlpha(0.373).get(), matrixStack);

        // Render Delayed Subtitles Hud
        if ( PuffConfig.ENABLE_CHAT_ANIMATION.getValue() ) {
            PuffProxiedRenderer.SUBTITLES_HUD.render(matrixStack);
        }
    }
}
