package net.krlite.pufferfish.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.equator.core.sprite.IdentifierSprite;
import net.krlite.equator.render.Equator;
import net.krlite.pufferfish.PuffMod;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ScreenshotFlashRenderer {
    public static float flashOpacity = 0.0F;

    // Textures
    public static final IdentifierSprite FLASH = IdentifierSprite.of(PuffMod.identifierBuilder.texture("misc", "flash"));

    public static void setOpacity(float opacity) {
        flashOpacity = opacity;
    }

    private static void lerp() {
        flashOpacity = MathHelper.clamp(MathHelper.lerp(0.265F, flashOpacity, -0.1F), 0.0F, 1.0F);
    }

    public static void renderScreenshotFlash(MatrixStack matrixStack) {
        if ( flashOpacity > 0 ) {
            new Equator(FLASH).renderFixedOverlay(matrixStack, new Color(1.0F, 1.0F, 1.0F, flashOpacity));
        }
    }

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> lerp());
    }



    public static void init() {
        registerEvents();
    }
}
