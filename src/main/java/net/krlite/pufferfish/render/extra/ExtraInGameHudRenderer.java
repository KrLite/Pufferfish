package net.krlite.pufferfish.render.extra;

import net.krlite.pufferfish.interaction_map.render.AnchorRenderer;
import net.krlite.pufferfish.interaction_map.util.AnchorProvider;
import net.krlite.pufferfish.math.PreciseColor;
import net.krlite.pufferfish.render.CameraOverlayHandler;
import net.krlite.pufferfish.util.AxisUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Optional;

public class ExtraInGameHudRenderer {
    public static void render(MatrixStack matrixStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        int
                width = client.getWindow().getScaledWidth(),
                height = client.getWindow().getScaledHeight();

        // Render Last Death Anchor
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Optional<Double> anchorPos = AnchorProvider.lastDeathPosition(player);
        Optional<Double> anchorDistance = AnchorProvider.lastDeathDistance(player);

        if ( anchorPos.isPresent() && anchorDistance.isPresent() ) {
            float opacity =
                    (float) MathHelper.clamp(
                            anchorDistance.get() >= 5
                                    ? (128 / anchorDistance.get())
                                    : Math.pow(anchorDistance.get() / 5, 1.7),
                            0, 1
                    );

            // Anchor
            AnchorRenderer.render(
                    matrixStack, ColorUtil.castAlpha(Color.WHITE, opacity),
                    (float) (double) anchorPos.get(), 540 * opacity
            );
        }

        // Render Urmeneta
        float
                posN = (float) AnchorProvider.powAngle(player, 90,  1.0072),
                posW = (float) AnchorProvider.powAngle(player, 0,   1.0072),
                posS = (float) AnchorProvider.powAngle(player, -90, 1.0072),
                posE = (float) AnchorProvider.powAngle(player, 180, 1.0072);

        float
                middleWidth = width / 2.0F,
                opacityN = (float) (1 - MathHelper.clamp(Math.pow(Math.abs(middleWidth - posN) / (middleWidth * 1.65), 2), 0, 1)),
                opacityW = (float) (1 - MathHelper.clamp(Math.pow(Math.abs(middleWidth - posW) / (middleWidth * 1.65), 2), 0, 1)),
                opacityS = (float) (1 - MathHelper.clamp(Math.pow(Math.abs(middleWidth - posS) / (middleWidth * 1.65), 2), 0, 1)),
                opacityE = (float) (1 - MathHelper.clamp(Math.pow(Math.abs(middleWidth - posE) / (middleWidth * 1.65), 2), 0, 1));

        // N
        AnchorRenderer.renderUrmeneta(
                ColorUtil.castAlpha(
                        Color.WHITE,
                        0.5F * opacityN
                ),
                posN, opacityN,
                AnchorRenderer.NORTH
        );

        // W
        AnchorRenderer.renderUrmeneta(
                ColorUtil.castAlpha(
                        Color.WHITE,
                        0.5F * opacityW
                ),
                posW, opacityW,
                AnchorRenderer.WEST
        );

        // S
        AnchorRenderer.renderUrmeneta(
                ColorUtil.castAlpha(
                        Color.WHITE,
                        0.5F * opacityS
                ),
                posS, opacityS,
                AnchorRenderer.SOUTH
        );

        // E
        AnchorRenderer.renderUrmeneta(
                ColorUtil.castAlpha(
                        Color.WHITE,
                        0.5F * opacityE
                ),
                posE, opacityE,
                AnchorRenderer.EAST
        );
    }
}
