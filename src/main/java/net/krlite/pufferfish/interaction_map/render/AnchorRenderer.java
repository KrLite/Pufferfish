package net.krlite.pufferfish.interaction_map.render;

import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;

public class AnchorRenderer {
    private static Identifier identifierBuilder(String textureName) {
        return IdentifierBuilder.texture("interaction_map", textureName);
    }

    public static final Identifier ANCHOR = identifierBuilder("anchor");



    public static void render(MatrixStack matrixStack, Color color, float center, float widgetSize) {
        renderSilkyWay(matrixStack, ColorUtil.castAlpha(color, color.getAlpha() / 255.0F * 0.45F), center, widgetSize);
        renderAnchor(color, center, MathHelper.clamp(widgetSize, 0, 16));
    }

    public static void renderSilkyWay(MatrixStack matrixStack, Color color, float center, float width) {
        renderSilkyWay(matrixStack, color, center, 0, width, 2.5F);
    }

    public static void renderSilkyWay(MatrixStack matrixStack, Color color, float center, float upperEdge, float width, float height) {
        PuffMod.CR.fillGradiantHorizontal(
                matrixStack,
                center - width / 2, upperEdge,
                center, upperEdge + height,
                ColorUtil.castAlpha(color),
                color
        );

        PuffMod.CR.fillGradiantHorizontal(
                matrixStack,
                center, upperEdge,
                center + width / 2, upperEdge + height,
                color,
                ColorUtil.castAlpha(color)
        );
    }

    public static void renderAnchor(Color color, float center, float width) {
        renderAnchor(color, center, 0, width, 2.5F);
    }

    public static void renderAnchor(Color color, float center, float upperEdge, float width, float height) {
        renderWidget(ANCHOR, color, center, upperEdge, width, height);
    }

    private static void renderWidget(Identifier identifier, Color color, float center, float upperEdge, float width, float height) {
        PuffMod.CTR.renderColoredTexture(
                identifier, color,
                center - width / 2, upperEdge,
                center + width / 2, upperEdge + height,
                0, 0, 1, 1
        );
    }
}
