package net.krlite.pufferfish.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;

public class ColoredRenderer extends DrawableHelper {
    public static void fill(
            MatrixStack matrixStack,
            float xBegin,   float yBegin,
            float xEnd,     float yEnd,
            Color color
    ) {
        fill(
                matrixStack.peek().getPositionMatrix(),
                xBegin, yBegin,
                xEnd,   yEnd,
                color);
    }

    public static void fill(
            Matrix4f matrix,
            float xBegin,   float yBegin,
            float xEnd,     float yEnd,
            Color color
    ) {
        if (xBegin < xEnd) {
            float t = xBegin;
            xBegin = xEnd;
            xEnd = t;
        }

        if (yBegin < yEnd) {
            float t = yBegin;
            yBegin = yEnd;
            yEnd = t;
        }

        float
                r = color.getRed() / 255.0F,
                g = color.getGreen() / 255.0F,
                b = color.getBlue() / 255.0F,
                a = color.getAlpha() / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder
                .vertex(matrix, xBegin, yEnd, 0.0F)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(matrix, xEnd, yEnd, 0.0F)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(matrix, xEnd, yBegin, 0.0F)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(matrix, xBegin, yBegin, 0.0F)
                .color(r, g, b, a)
                .next();

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
