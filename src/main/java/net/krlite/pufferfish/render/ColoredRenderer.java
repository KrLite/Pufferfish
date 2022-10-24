package net.krlite.pufferfish.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;

public class ColoredRenderer extends DrawableHelper {
    public static void fillColoredVertical(
            MatrixStack matrixStack,
            float xBegin,   float yBegin,
            float xEnd,     float yEnd,
            Color colorUpper,  Color colorLower
    ) {
        fillColored(
                matrixStack,
                xBegin, yBegin,
                xEnd,   yEnd,
                colorUpper, colorLower,
                colorLower, colorUpper

        );
    }

    public static void fillColoredHorizontal(
            MatrixStack matrixStack,
            float xBegin,   float yBegin,
            float xEnd,     float yEnd,
            Color colorLeft,  Color colorRight
    ) {
        fillColored(
                matrixStack,
                xBegin, yBegin,
                xEnd,   yEnd,
                colorLeft,  colorLeft,
                colorRight, colorRight

        );
    }

    public static void fillColored(
            MatrixStack matrixStack,
            float xBegin,   float yBegin,
            float xEnd,     float yEnd,
            Color colorLU,  Color colorLD,
            Color colorRD,  Color colorRU
    ) {
        fillColored(
                matrixStack.peek().getPositionMatrix(),
                xBegin, yBegin,
                xBegin, yEnd,
                xEnd,   yEnd,
                xEnd,   yBegin,
                colorLU, colorLD, colorRD, colorRU
        );
    }

    /**
     * Renders a colorized field in four specified vertices.
     *
     * @param matrix    target matrix.
     * @param xLU       x left-up,
     * @param yLU       y left-up;
     * @param xLD       x left-down,
     * @param yLD       y left-down;
     * @param xRD       x right-down,
     * @param yRD       y right-down;
     * @param xRU       x right-up,
     * @param yRU       y right-up.
     * @param colorLU   color of the left-up vertex;
     * @param colorLD   color of the left-down vertex;
     * @param colorRD   color of the right-up vertex;
     * @param colorRU   color of the right-down vertex.
     */
    public static void fillColored(
            Matrix4f matrix,
            float xLU,  float yLU,
            float xLD,  float yLD,
            float xRD,  float yRD,
            float xRU,  float yRU,
            Color colorLU, Color colorLD,
            Color colorRD, Color colorRU
    ) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder
                .vertex(matrix, xLD, yLD, 0.0F)
                .color(
                        colorLD.getRed() / 255.0F,
                        colorLD.getGreen() / 255.0F,
                        colorLD.getBlue() / 255.0F,
                        colorLD.getAlpha() / 255.0F
                )
                .next();

        bufferBuilder
                .vertex(matrix, xRD, yRD, 0.0F)
                .color(
                        colorRD.getRed() / 255.0F,
                        colorRD.getGreen() / 255.0F,
                        colorRD.getBlue() / 255.0F,
                        colorRD.getAlpha() / 255.0F
                )
                .next();

        bufferBuilder
                .vertex(matrix, xRU, yRU, 0.0F)
                .color(
                        colorRU.getRed() / 255.0F,
                        colorRU.getGreen() / 255.0F,
                        colorRU.getBlue() / 255.0F,
                        colorRU.getAlpha() / 255.0F
                )
                .next();

        bufferBuilder
                .vertex(matrix, xLU, yLU, 0.0F)
                .color(
                        colorLU.getRed() / 255.0F,
                        colorLU.getGreen() / 255.0F,
                        colorLU.getBlue() / 255.0F,
                        colorLU.getAlpha() / 255.0F
                )
                .next();

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
