package net.krlite.pufferfish.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

import java.awt.*;

public class DynamicColoredTextureRenderer extends DrawableHelper {
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

    public static void renderDynamicColoredOverlay(Identifier identifier, Color color, float width, float height) {
        renderPositionedDynamicColoredTexture(
                identifier, color,
                0.0F, 0.0F, width, height,
                0.0F, 0.0F, 1.0F, 1.0F
        );
    }

    public static void renderPositionedDynamicColoredTexture(
            Identifier identifier, Color color,
            float xBegin, float yBegin, float xEnd, float yEnd,
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
                .vertex(xBegin, yEnd, -90.0)
                .texture(uBegin, vEnd)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(xEnd, yEnd, -90.0)
                .texture(uEnd, vEnd)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(xEnd, yBegin, -90.0)
                .texture(uEnd, vBegin)
                .color(r, g, b, a)
                .next();

        bufferBuilder
                .vertex(xBegin, yBegin, -90.0)
                .texture(uBegin, vBegin)
                .color(r, g, b, a)
                .next();
        tessellator.draw();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
