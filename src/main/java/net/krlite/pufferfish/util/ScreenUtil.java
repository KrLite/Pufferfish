package net.krlite.pufferfish.util;

import net.krlite.equator.core.MatrixWrapper;
import net.krlite.equator.core.sprite.IdentifierSprite;
import net.krlite.equator.render.Equator;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.render.PuffRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ScreenUtil extends DrawableHelper {
    public static final Color backgroundColorUpper = new Color(0xC0101010, true);
    public static final Color backgroundColorLower = new Color(0xD0101010, true);
    private static boolean show = false;
    public static double opacity = 0, loadingOpacity = 0;

    // Textures
    public static final IdentifierSprite AMBIENT = IdentifierSprite.of(PuffMod.identifierBuilder.texture("overlay", "ambient"));

    public static void renderBackground() {
        Equator.Colors.fill(
                new MatrixWrapper(
                        new MatrixStack(),
                        0, 0,
                        MinecraftClient.getInstance().getWindow().getScaledWidth(),
                        MinecraftClient.getInstance().getWindow().getScaledHeight()
                ), Color.BLACK
        );

        new Equator(AMBIENT).renderScaledOverlay(
                new MatrixStack(), ColorUtil.castAlpha(Color.WHITE, (float) loadingOpacity * 0.45F), 1
        );

        Equator.Colors.fill(
                new MatrixWrapper(
                        new MatrixStack(),
                        0, MinecraftClient.getInstance().getWindow().getScaledHeight() - 32.0F,
                        MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight() - 31.3F
                ), ColorUtil.castAlpha(new Color(80, 82, 93, 255), (float) loadingOpacity)
        );
    }

    public static void update(double delta) {
        opacity = MathHelper.lerp(delta, opacity, show ? 1 : 0);

        show = MinecraftClient.getInstance().currentScreen != null && show;
    }

    public static void updateLoading(double delta, boolean show) {
        loadingOpacity = MathHelper.lerp(delta, loadingOpacity, show ? 1 : 0);
    }

    public static void setOpacity(double opacity) {
        ScreenUtil.opacity = opacity;
        show(true);
    }

    public static void show(boolean show) {
        ScreenUtil.show = show;
    }
}
