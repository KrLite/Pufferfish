package net.krlite.pufferfish.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

import java.awt.*;

public class DynamicColoredTextureRenderer extends DrawableHelper {
    /**
     * Renders a centered colorized texture in square by the default size of 256 pixels, a simple version of {@link #renderCenteredDynamicColoredTexture(Identifier, Color, float, float, int, int, int, int, int) RenderCenteredDynamicColoredTexture.}
     */
    public static void renderCenteredDynamicColoredTexture(
            Identifier identifier, Color color,
            float xCentered, float yCentered,
            int x, int y, int width, int height
    ) {
        renderCenteredDynamicColoredTexture(
                identifier, color,
                xCentered, yCentered,
                x, y, width, height,
                256
        );
    }

    /**
     * Renders a colorized texture in square by the default size of 256 pixels, a simple version of {@link #renderDynamicColoredTexture(Identifier, Color, float, float, int, int, int, int, int) RenderDynamicColoredTexture.}
     */
    public static void renderDynamicColoredTexture(
            Identifier identifier, Color color,
            float xPos, float yPos,
            int x, int y, int width, int height
    ) {
        renderDynamicColoredTexture(
                identifier, color,
                xPos, yPos,
                x, y, width, height,
                256
        );
    }

    /**
     * Renders a centered colorized texture in square, a simple version of {@link #renderPositionedDynamicColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float) RenderPositionedDynamicColoredTexture.}
     * @param xCentered     x center,
     * @param yCentered     y center;
     * @param x             x left-up in texture uv,
     * @param y             y left-up in texture uv.
     * @param textureSize   width and height of the target texture.
     */
    public static void renderCenteredDynamicColoredTexture(
            Identifier identifier, Color color,
            float xCentered, float yCentered,
            int x, int y, int width, int height,
            int textureSize
    ) {
        float xPos = xCentered - width / 2.0F, yPos = yCentered + height / 2.0F;

        renderPositionedDynamicColoredTexture(
                identifier, color,
                xPos, yPos, xPos + width, xPos + height,
                (float) x / textureSize, (float) y / textureSize,
                (float) (x + width) / textureSize, (float) (y + width) / textureSize
        );
    }

    /**
     * Renders a colorized texture in square, a simple version of {@link #renderPositionedDynamicColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float) RenderPositionedDynamicColoredTexture.}
     * @param xPos          x left-up,
     * @param yPos          y left-up;
     * @param x             x left-up in texture uv,
     * @param y             y left-up in texture uv.
     * @param textureSize   width and height of the target texture.
     */
    public static void renderDynamicColoredTexture(
            Identifier identifier, Color color,
            float xPos, float yPos,
            int x, int y, int width, int height,
            int textureSize
    ) {
        renderPositionedDynamicColoredTexture(
                identifier, color,
                xPos, yPos, xPos + width, xPos + height,
                (float) x / textureSize, (float) y / textureSize,
                (float) (x + width) / textureSize, (float) (y + width) / textureSize
        );
    }

    /**
     * Renders a colorized camera overlay.
     * @see #renderPositionedDynamicColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float) RenderPositionedDynamicColoredTexture
     */
    public static void renderDynamicColoredOverlay(Identifier identifier, Color color, float width, float height) {
        renderPositionedDynamicColoredTexture(
                identifier, color,
                0.0F, 0.0F, width, height,
                0.0F, 0.0F, 1.0F, 1.0F
        );
    }

    /**
     * Renders a colorized texture in square.
     * @see  #renderPrecisionPositionedDynamicColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float, float, float, float, float) RenderPrecisionPositionedDynamicColoredTexture
     */
    public static void renderPositionedDynamicColoredTexture(
            Identifier identifier, Color color,
            float xBegin, float yBegin, float xEnd, float yEnd,
            float uBegin, float vBegin, float uEnd, float vEnd
    ) {
        renderPrecisionPositionedDynamicColoredTexture(
                identifier, color,
                xBegin, yBegin,
                xBegin, yEnd,
                xEnd,   yBegin,
                xEnd,   yEnd,
                uBegin, vBegin, uEnd, vEnd
        );
    }

    /**
     * Renders a colorized texture in four specified vertices.
     *
     * @param identifier    identifier for target texture.
     * @param color         target Java Color, applies on the texture.
     * @param xLU           x left-up,
     * @param yLU           y left-up;
     * @param xLD           x left-down,
     * @param yLD           y left-down;
     * @param xRU           x right-up,
     * @param yRU           y right-up;
     * @param xRD           x right-down,
     * @param yRD           y right-down.
     * @param uBegin        texture u begin,
     * @param vBegin        texture v begin;
     * @param uEnd          texture u end,
     * @param vEnd          texture v end.
     */
    public static void renderPrecisionPositionedDynamicColoredTexture(
            Identifier identifier, Color color,
            float xLU, float yLU,
            float xLD, float yLD,
            float xRU, float yRU,
            float xRD, float yRD,
            float uBegin, float vBegin, float uEnd, float vEnd
    ) {
        int
                r = color.getRed(),
                g = color.getGreen(),
                b = color.getBlue(),
                a = color.getAlpha();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
        RenderSystem.setShaderTexture(0, identifier);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder
                .vertex(xRU, yRU, -90.0)
                .texture(uBegin, vEnd)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(xRD, yRD, -90.0)
                .texture(uEnd, vEnd)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(xLD, yLD, -90.0)
                .texture(uEnd, vBegin)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(xLU, yLU, -90.0)
                .texture(uBegin, vBegin)
                .color(r, g, b, a)
                .next();
        tessellator.draw();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
