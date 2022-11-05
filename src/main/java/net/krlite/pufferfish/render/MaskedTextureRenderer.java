package net.krlite.pufferfish.render;

import net.krlite.pufferfish.util.Solver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import oshi.util.tuples.Pair;

import java.awt.*;

public class MaskedTextureRenderer extends ColoredTextureRenderer {
    public MaskedTextureRenderer() {

    }

    /**
     * Renders a centered colorized texture in a limited square area
     * by the default size of 256 pixels, a simple version of
     *
     * {@link
     * #renderCenteredColoredTexture(Identifier, Color, float, float, int, int, int, int, int, float, float, float, float)
     * renderCenteredColoredTexture.}
     */
    public void renderCenteredColoredTexture(
            Identifier identifier, Color color,
            float xCentered,    float yCentered,
            int x,              int y,
            int width,          int height,
            float xMin,         float yMin,
            float xMax,         float yMax
    ) {
        renderCenteredColoredTexture(
                identifier, color,
                xCentered,  yCentered,
                x,          y,
                width,      height,
                256,
                xMin,       yMin,
                xMax,       yMax
        );
    }

    /**
     * Renders a colorized texture in a limited square area by the
     * default size of 256 pixels, a simple version of
     *
     * {@link
     * #renderPositionedColoredTexture(Identifier, Color, float, float, int, int, int, int, int, float, float, float, float)
     * renderPositionedColoredTexture.}
     */
    public void renderPositionedColoredTexture(
            Identifier identifier, Color color,
            float xPos, float yPos,
            int x,      int y,
            int width,  int height,
            float xMin, float yMin,
            float xMax, float yMax
    ) {
        renderPositionedColoredTexture(
                identifier, color,
                xPos,   yPos,
                x,      y,
                width,  height,
                256,
                xMin,   yMin,
                xMax,   yMax
        );
    }

    /**
     * Renders a centered colorized texture in a limited square
     * area, a simple version of
     *
     * {@link
     * #renderColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float)
     * renderColoredTexture.}
     *
     * @param xCentered     x center,
     * @param yCentered     y center;
     * @param x             x left-up in texture uv,
     * @param y             y left-up in texture uv.
     * @param textureSize   width and height of the target texture.
     * @param xMin          minimal allowed x,
     * @param yMin          minimal allowed y;
     * @param xMax          maximal allowed x,
     * @param yMax          maximal allowed y.
     */
    public void renderCenteredColoredTexture(
            Identifier identifier, Color color,
            float xCentered,    float yCentered,
            int x,              int y,
            int width,          int height,
            int textureSize,
            float xMin,         float yMin,
            float xMax,         float yMax
    ) {
        float xPos = xCentered - width / 2.0F, yPos = yCentered + height / 2.0F;

        renderColoredTexture(
                identifier, color,
                xPos, yPos, xPos + width, xPos + height,
                (float) x / textureSize, (float) y / textureSize,
                (float) (x + width) / textureSize, (float) (y + width) / textureSize,
                xMin, yMin, xMax, yMax
        );
    }

    /**
     * Renders a colorized texture in a limited square area,
     * a simple version of
     *
     * {@link
     * #renderColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float)
     * renderColoredTexture.}
     *
     * @param xPos          x left-up,
     * @param yPos          y left-up;
     * @param x             x left-up in texture uv,
     * @param y             y left-up in texture uv.
     * @param textureSize   width and height of the target texture.
     * @param xMin          minimal allowed x,
     * @param yMin          minimal allowed y;
     * @param xMax          maximal allowed x,
     * @param yMax          maximal allowed y.
     */
    public void renderPositionedColoredTexture(
            Identifier identifier, Color color,
            float xPos, float yPos,
            int x,      int y,
            int width,  int height,
            int textureSize,
            float xMin, float yMin,
            float xMax, float yMax
    ) {
        renderColoredTexture(
                identifier, color,
                xPos, yPos, xPos + width, xPos + height,
                (float) x / textureSize, (float) y / textureSize,
                (float) (x + width) / textureSize, (float) (y + width) / textureSize,
                xMin, yMin, xMax, yMax
        );
    }

    /**
     * Renders a colorized camera overlay in a limited square area.
     *
     * @see
     * #renderColoredTexture(Identifier, Color, float, float, float, float, float, float, float, float)
     * renderColoredTexture
     */
    public void renderColoredOverlay(
            Identifier identifier, Color color,
            float xMin, float yMin,
            float xMax, float yMax
    ) {
        renderColoredTexture(
                identifier, color,
                0.0F, 0.0F,
                MinecraftClient.getInstance().getWindow().getScaledWidth(),
                MinecraftClient.getInstance().getWindow().getScaledHeight(),
                0.0F, 0.0F, 1.0F, 1.0F,
                xMin, yMin, xMax, yMax
        );
    }

    /**
     * Renders a colorized texture in a limited square area.
     */
    public void renderColoredTexture(
            Identifier identifier, Color color,
            float xBegin,   float yBegin,
            float xEnd,     float yEnd,
            float uBegin,   float vBegin,
            float uEnd,     float vEnd,
            float xMin,     float yMin,
            float xMax,     float yMax
    ) {
        Pair<Pair<Float, Float>, Pair<Float, Float>> grid = Solver.gridXY(xBegin, yBegin, xEnd, yEnd);

        xBegin  = grid.getA().getA();
        xEnd    = grid.getA().getB();
        yBegin  = grid.getB().getA();
        yEnd    = grid.getB().getB();

        Pair<Pair<Float, Float>, Pair<Float, Float>> gridMask = Solver.gridXY(xMin, yMin, xMax, yMax);

        xMin = gridMask.getA().getA();
        xMax = gridMask.getA().getB();
        yMin = gridMask.getB().getA();
        yMax = gridMask.getB().getB();

        float
                width   = xEnd - xBegin,
                height  = yEnd - yBegin;

        super.renderColoredTexture(
                identifier, color,
                Math.max(xBegin, xMin), Math.max(yBegin, yMin),
                Math.min(xEnd, xMax),   Math.min(yEnd, yMax),
                xBegin < xMin
                        ? uBegin + (uEnd - uBegin) * (xMin - xBegin) / width
                        : uBegin,
                yBegin < yMin
                        ? vBegin + (vEnd - vBegin) * (yMin - yBegin) / height
                        : vBegin,
                xEnd > xMax
                        ? uEnd - (uEnd - uBegin) * (xEnd - xMax) / width
                        : vEnd,
                yEnd > yMax
                        ? vEnd + (vEnd - vBegin) * (yEnd - yMax) / height
                        : vEnd
        );
    }
}
