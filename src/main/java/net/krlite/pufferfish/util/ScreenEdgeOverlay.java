package net.krlite.pufferfish.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static net.krlite.pufferfish.util.AxisLocker.*;

public class ScreenEdgeOverlay {
    public enum Color {
        NONE(0, 0, 0, 0),

        // Default Color
        WHITE(255, 255, 255, 1),
        BLACK(0, 0, 0, 1),

        RED(255, 0, 0, 1),
        GREEN(0, 255, 0, 1),
        BLUE(0, 0, 255, 1),

        // Custom Color
        OCEAN(33, 28, 126, 0.32F),
        SCARLET(189, 18, 55, 0.32F),
        VIOLET(120, 18, 118, 0.32F);

        private float red, green, blue, alpha;

        Color(float r, float g, float b, float a) {
            red = r;
            green = g;
            blue = b;
            alpha = a;
        }

        public void setColor(Color color) {
            red = color.red;
            green = color.green;
            blue = color.blue;
            alpha = color.alpha;
        }

        public void setColor(float r, float g, float b, float a) {
            red = r;
            green = g;
            blue = b;
            alpha = a;
        }

        /**
         * @param   index   the index of the target color.
         * <ul><li>0 Alpha
         * <li>1 Red
         * <li>2 Green
         * <li>3 Blue
         * </ul>
         */
        public float getColor(int index) {
            return index == 0
                    ? alpha
                    : index == 1
                        ? red
                        : index == 2
                            ? green
                            : index == 3
                                ? blue
                                : 0.0F;
        }
    }

    public static Color targetColor = Color.NONE;

    public static float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 0.0F;

    public static void setColor(Color color) {
        red = color.red;
        green = color.green;
        blue = color.blue;
        alpha = color.alpha;
    }

    public static void setColor(float r, float g, float b, float a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }



    private static final float delta = 0.17F;

    private static Identifier identifierBuilder(String textureName) {
        return new Identifier(PuffMod.MOD_ID, "textures/overlay/" + textureName + ".png");
    }

    // Final Identifier
    public static final Identifier NONE = identifierBuilder("transparent");
    public static final Identifier WH = identifierBuilder("white");
    public static final Identifier BK = identifierBuilder("black");
    public static final Identifier R = identifierBuilder("red");
    public static final Identifier G = identifierBuilder("green");
    public static final Identifier B = identifierBuilder("blue");

    private static void lerp() {
        setColor(
                MathHelper.lerp(delta, red, targetColor.red),
                MathHelper.lerp(delta, green, targetColor.green),
                MathHelper.lerp(delta, blue, targetColor.blue),
                MathHelper.lerp(delta, alpha, targetColor.alpha)
        );
    }

    private static void adjustOverlay() {
        // Adjust Overlay State
        targetColor =
                axisLock.get(Axis.PITCH)
                        ? axisLock.get(Axis.YAW)
                                ? ScreenEdgeOverlay.Color.VIOLET
                                : ScreenEdgeOverlay.Color.OCEAN
                        : axisLock.get(Axis.YAW)
                                ? ScreenEdgeOverlay.Color.SCARLET
                                : ScreenEdgeOverlay.Color.NONE;
    }

    public static void update() {
        lerp();
        adjustOverlay();
    }
}
