package net.krlite.pufferfish.render;

import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

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
        if ( color.getAlpha() / 255.0F > 0 ) PuffMod.CTR.renderColoredOverlay(identifier, color);
    }
}
