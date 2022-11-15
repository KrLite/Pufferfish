package net.krlite.pufferfish.math;

import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mutable;

import java.awt.*;
import java.util.Objects;

public class PreciseColor {
    private double red;
    private double green;
    private double blue;
    private double alpha;

    /**
     * Creates a color which every value is precisely stored as double.
     *
     * @param red       red (double 0 to 1),
     * @param green     green (double 0 to 1),
     * @param blue      blue (double 0 to 1),
     * @param alpha     alpha (double 0 to 1).
     */
    private PreciseColor(double red, double green, double blue, double alpha) {
        this.red    = Double.isNaN(red) ? Double.NaN : MathHelper.clamp(red, 0, 1);
        this.green  = Double.isNaN(green) ? Double.NaN : MathHelper.clamp(green, 0, 1);
        this.blue   = Double.isNaN(blue) ? Double.NaN : MathHelper.clamp(blue, 0, 1);
        this.alpha  = Double.isNaN(alpha) ? Double.NaN : MathHelper.clamp(alpha, 0, 1);
    }

    /**
     * Checks if the value is between zero and one.
     *
     * @param value     the dedicated value.
     * @return          <tt>true</tt> if the value is in the range of 0 to 1, otherwise <tt>false</tt>.
     */
    private boolean is(double value) {
        return !Double.isNaN(value) && (value >= 0 && value <= 1);
    }

    /**
     * Interpolate the value by delta.
     *
     * @param value     the original value,
     * @param target    the target value,
     * @param delta     delta index.
     * @return          returns the value interpolated from target by delta.
     */
    private double lerp(double value, double target, double delta) {
        return is(value)
                ? is(target)
                        ? value + (target - value) * delta
                        : value
                : is(target)
                        ? target
                        : -1;
    }

    /**
     * An empty color (with no RGB) which is translucent.
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull PreciseColor empty() {
        return new PreciseColor(Double.NaN, Double.NaN, Double.NaN, 0);
    }

    /**
     * An empty color (with no RGB) which is opaque.
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull PreciseColor full() {
        return new PreciseColor(Double.NaN, Double.NaN, Double.NaN, 1);
    }

    /**
     * Creates a {@link  PreciseColor} from the RGBA values.
     *
     * @return       returns a {@link PreciseColor PreciseColor.}
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull PreciseColor of(double red, double green, double blue, double alpha) {
        return new PreciseColor(red, green, blue, alpha);
    }

    /**
     * Creates an opaque {@link PreciseColor} from the RGB values.
     *
     * @return      returns an opaque {@link PreciseColor PreciseColor.}
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull PreciseColor of(double red, double green, double blue) {
        return of(red, green, blue, 1);
    }

    /**
     * Creates a {@link PreciseColor} from the RGB values which is opaque or translucent.
     *
     * @param opaque    whether the color is opaque or translucent.
     * @return          returns an opaque or translucent {@link PreciseColor PreciseColor.}
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull PreciseColor of(double red, double green, double blue, boolean opaque) {
        return of(red, green, blue, opaque ? 1 : 0);
    }

    /**
     * Creates a {@link PreciseColor} from the dedicated {@link Color Color.}
     *
     * @param color     the dedicated {@link Color Color.}
     * @return          returns a {@link PreciseColor PreciseColor.}
     */
    @Contract("_ -> new")
    public static @NotNull PreciseColor of(@NotNull Color color) {
        return new PreciseColor(
                color.getRed() / 255.0,
                color.getGreen() / 255.0,
                color.getBlue() / 255.0,
                color.getAlpha() / 255.0
        );
    }

    public PreciseColor independent() {
        return new PreciseColor(this.red, this.green, this.blue, this.alpha);
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getAlpha() {
        return alpha;
    }

    /**
     * Gets the {@link PreciseColor} as a {@link Color Color.}
     *
     * @return      returns a {@link Color} same as the self.
     */
    public Color get() {
        return new Color(
                (float) (is(red) ? red : 0),
                (float) (is(green) ? green : 0),
                (float) (is(blue) ? blue : 0),
                (float) (is(alpha) ? alpha : 0)
        );
    }

    /**
     * Gets the color's RGBA integer value.
     */
    public int getRGBA() {
        return get().getRGB();
    }

    /**
     * Replaces the red value.
     */
    public PreciseColor castRed(double red) {
        this.red = Double.isNaN(red) ? Double.NaN : MathHelper.clamp(red, 0, 1);
        return this;
    }

    /**
     * Replaces the green value.
     */
    public PreciseColor castGreen(double green) {
        this.green = Double.isNaN(green) ? Double.NaN : MathHelper.clamp(green, 0, 1);
        return this;
    }

    /**
     * Replaces the blue value.
     */
    public PreciseColor castBlue(double blue) {
        this.blue = Double.isNaN(blue) ? Double.NaN : MathHelper.clamp(blue, 0, 1);
        return this;
    }

    /**
     * Replaces the alpha value.
     */
    public PreciseColor castAlpha(double alpha) {
        this.alpha = Double.isNaN(alpha) ? Double.NaN : MathHelper.clamp(alpha, 0, 1);
        return this;
    }

    /**
     * Multiples the red value.
     */
    public PreciseColor multipleRed(double multiplier) {
        return this.castRed(red * multiplier);
    }

    /**
     * Multiples the green value.
     */
    public PreciseColor multipleGreen(double multiplier) {
        return this.castGreen(green * multiplier);
    }

    /**
     * Multiples the blue value.
     */
    public PreciseColor multipleBlue(double multiplier) {
        return this.castBlue(blue * multiplier);
    }

    /**
     * Multiples the alpha value.
     */
    public PreciseColor multipleAlpha(double multiplier) {
        return this.castAlpha(alpha * multiplier);
    }

    /**
     * Blends the self with a {@link PreciseColor PreciseColor.}
     *
     * @param color     the dedicated {@link PreciseColor PreciseColor.}
     * @return          returns a blended {@link PreciseColor PreciseColor.}
     */
    public void blend(PreciseColor color) {
        blend(color, 0.5);
    }

    /**
     * Blends the self with a {@link PreciseColor} in an index.
     *
     * @param color     the dedicated {@link PreciseColor PreciseColor,}
     * @param delta     the index.
     * @return          returns a blended {@link PreciseColor PreciseColor.}
     */
    public void blend(@NotNull PreciseColor color, double delta) {
        castRed(lerp(this.red, color.red, delta));
        castGreen(lerp(this.green, color.green, delta));
        castBlue(lerp(this.blue, color.blue, delta));
        castAlpha(lerp(this.alpha, color.alpha, delta));
    }

    /**
     * Gets the color in string.
     */
    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + ",a=" + getAlpha() + "]";
    }

    /**
     * Gets the color in hex string.
     */
    public String toHexString() {
        return toHexString(false);
    }

    /**
     * Gets the color in hex string.
     *
     * @param upperCase     whether uppercase the string or not.
     */
    public String toHexString(boolean upperCase) {
        return upperCase ? Integer.toHexString(getRGBA()).toUpperCase() : Integer.toHexString(getRGBA());
    }

    /**
     * Decodes a hex string and gets its dedicated {@link PreciseColor PreciseColor.}
     *
     * @param hexString     the hex string.
     * @return              returns a {@link PreciseColor} in which the value equals the hex string.
     */
    public static PreciseColor decode(String hexString) {
        return PreciseColor.of(Color.decode(hexString));
    }

    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }
}
