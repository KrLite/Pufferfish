package net.krlite.pufferfish.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.interaction_map.render.AnchorRenderer;
import net.krlite.pufferfish.interaction_map.util.ClientAnchorProvider;
import net.krlite.pufferfish.util.AxisLocker;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ExtraInGameHudRenderer {
    private static MatrixStack matrixStack;
    private static Color axisColor = ColorUtil.TRANSLUCENT;

    public static void setMatrixStack(MatrixStack matrixStack) {
        ExtraInGameHudRenderer.matrixStack = matrixStack;
    }

    public static void render() {
        if ( matrixStack != null ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

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

            // Render Flash
            ScreenshotFlashRenderer.renderScreenshotFlash();
        }
    }
}
