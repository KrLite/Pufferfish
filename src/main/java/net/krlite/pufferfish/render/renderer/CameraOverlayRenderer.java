package net.krlite.pufferfish.render.renderer;

import net.krlite.equator.render.Equator;
import net.krlite.pufferfish.PuffMod;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class CameraOverlayRenderer {
    private static Identifier identifierBuilder(String textureName) {
        return PuffMod.identifierBuilder.texture("overlay", textureName);
    }

    // Texture
    public static final Identifier NONE = identifierBuilder("transparent");
    public static final Identifier BASIC = identifierBuilder("basic");

    public static void renderCameraOverlay(Color color, MatrixStack matrixStack) {
        renderCameraOverlay(BASIC, color, matrixStack);
    }

    public static void renderCameraOverlay(Identifier identifier, Color color, MatrixStack matrixStack) {
        if ( color.getAlpha() / 255.0F > 0 ) {
            new Equator(identifier).renderOverlay(matrixStack, color);
        }
    }
}
