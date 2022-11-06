package net.krlite.pufferfish.render;

import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.util.Identifier;

import java.awt.*;

public class CameraOverlayHandler {
    private static Identifier identifierBuilder(String textureName) {
        return IdentifierBuilder.texture("overlay", textureName);
    }

    // Texture
    public static final Identifier NONE = identifierBuilder("transparent");
    public static final Identifier BASIC = identifierBuilder("basic");

    public static void renderCameraOverlay(Color color) {
        renderCameraOverlay(BASIC, color);
    }

    public static void renderCameraOverlay(Identifier identifier, Color color) {
        if ( color.getAlpha() / 255.0F > 0 ) PuffRenderer.COLOR_TEXTURE.renderColoredOverlay(identifier, color);
    }
}
