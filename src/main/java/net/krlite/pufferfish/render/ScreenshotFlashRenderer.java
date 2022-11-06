package net.krlite.pufferfish.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ScreenshotFlashRenderer {
    public static float flashOpacity = 0.0F;
    public static final Identifier FLASH = IdentifierBuilder.texture("misc", "flash");

    public static void setOpacity(float opacity) {
        flashOpacity = opacity;
    }

    private static void lerp() {
        flashOpacity = MathHelper.clamp(MathHelper.lerp(0.265F, flashOpacity, -0.1F), 0.0F, 1.0F);
    }

    public static void renderScreenshotFlash() {
        if ( flashOpacity > 0 ) {
            PuffRenderer.COLOR_TEXTURE.renderFixedColoredOverlay(FLASH, new Color(1.0F, 1.0F, 1.0F, flashOpacity));
        }
    }

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> lerp());
    }



    public static void init() {
        registerEvents();
    }
}
