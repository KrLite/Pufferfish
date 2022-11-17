package net.krlite.pufferfish.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.math.IdentifierSprite;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ScreenshotFlashRenderer {
    public static float flashOpacity = 0.0F;

    // Textures
    public static final IdentifierSprite FLASH = IdentifierSprite.of(IdentifierBuilder.texture("misc", "flash"));

    public static void setOpacity(float opacity) {
        flashOpacity = opacity;
    }

    private static void lerp() {
        flashOpacity = MathHelper.clamp(MathHelper.lerp(0.265F, flashOpacity, -0.1F), 0.0F, 1.0F);
    }

    public static void renderScreenshotFlash(MatrixStack matrixStack) {
        if ( flashOpacity > 0 ) {
            PuffRenderer.COLORED_TEXTURE.renderFixedColoredOverlay(FLASH.getIdentifier(), new Color(1.0F, 1.0F, 1.0F, flashOpacity), matrixStack);
        }
    }

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> lerp());
    }



    public static void init() {
        registerEvents();
    }
}
