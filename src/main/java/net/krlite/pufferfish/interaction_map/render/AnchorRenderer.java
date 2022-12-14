package net.krlite.pufferfish.interaction_map.render;

import net.krlite.equator.core.MatrixWrapper;
import net.krlite.equator.core.sprite.HorizontalSprite;
import net.krlite.equator.core.sprite.IdentifierSprite;
import net.krlite.equator.render.Equator;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class AnchorRenderer {
    private static Identifier identifierBuilder(String textureName) {
        return PuffMod.identifierBuilder.texture("interaction_map", textureName);
    }

    // Identifiers
    public static final Identifier ANCHOR = identifierBuilder("anchor");
    public static final HorizontalSprite URMENETA = new HorizontalSprite(identifierBuilder("urmeneta"), 4);

    // Identifier Sprites
    public static final IdentifierSprite NORTH = URMENETA.get(1);
    public static final IdentifierSprite WEST = URMENETA.get(2);
    public static final IdentifierSprite SOUTH = URMENETA.get(3);
    public static final IdentifierSprite EAST = URMENETA.get(4);



    public static void render(MatrixStack matrixStack, Color color, float center, float widgetSize) {
        renderSilkyWay(
                matrixStack,
                ColorUtil.castAlpha(
                        color, color.getAlpha() / 255.0F * 0.585F
                ),
                center, widgetSize
        );

        renderAnchor(
                color, center,
                (float) Math.sqrt(
                        widgetSize / MinecraftClient.getInstance().getWindow().getScaledWidth() / 10.0F
                ) * MinecraftClient.getInstance().getWindow().getScaledWidth() / 10.0F
        );
    }

    public static void renderSilkyWay(MatrixStack matrixStack, Color color, float center, float width) {
        renderSilkyWay(matrixStack, color, center, -2, width, 6.0F);
    }

    public static void renderSilkyWay(MatrixStack matrixStack, Color color, float center, float upperEdge, float width, float height) {
        Equator.Colors.gradientHorizontal(
                new MatrixWrapper(
                        matrixStack,
                        center - width / 2, upperEdge,
                        center, upperEdge + height
                ), ColorUtil.castAlpha(color), color
        );

        Equator.Colors.gradientHorizontal(
                new MatrixWrapper(
                        matrixStack,
                        center, upperEdge,
                        center + width / 2, upperEdge + height
                ), color, ColorUtil.castAlpha(color)
        );
    }

    public static void renderAnchor(Color color, float center, float width) {
        renderAnchor(color, center, -2, width, 6);
    }

    public static void renderAnchor(Color color, float center, float upperEdge, float width, float height) {
        renderWidget(ANCHOR, color, center, upperEdge, width, height);
    }

    public static void renderUrmeneta(Color color, float center, float widgetScale, IdentifierSprite direction) {
        renderUrmeneta(color, center, 8 + 5.0F * widgetScale, 8 * widgetScale, direction);
    }

    public static void renderUrmeneta(Color color, float center, float upperEdge, float widgetSize, IdentifierSprite direction) {
        renderWidget(
                direction, color, center, upperEdge, widgetSize, widgetSize
        );
    }

    private static void renderWidget(Identifier identifier, Color color, float center, float upperEdge, float width, float height) {
        renderWidget(identifier, color, center, upperEdge, width, height, 0, 0, 1, 1);
    }

    private static void renderWidget(IdentifierSprite identifierSprite, Color color, float center, float upperEdge, float width, float height) {
        new Equator(identifierSprite).render(
                new MatrixStack(), color,
                center - width / 2, upperEdge,
                center + width / 2, upperEdge + height
        );
    }

    private static void renderWidget(Identifier identifier, Color color, float center, float upperEdge, float width, float height, float uBegin, float vBegin, float uEnd, float vEnd) {
        new Equator(
                new IdentifierSprite(
                        identifier, uBegin, vBegin, uEnd, vEnd
                )
        ).render(
                new MatrixStack(), color,
                center - width / 2, upperEdge,
                center + width / 2, upperEdge + height
        );
    }
}
