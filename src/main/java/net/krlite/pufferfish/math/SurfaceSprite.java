package net.krlite.pufferfish.math;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SurfaceSprite {
    private final Identifier identifier;
    private final int stepX, stepY;

    private SurfaceSprite(Identifier identifier, int stepX, int stepY) {
        this.identifier = identifier;
        this.stepX      = stepX;
        this.stepY      = stepY;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull SurfaceSprite create(Identifier identifier, int stepX, int stepY) {
        return new SurfaceSprite(
                identifier,
                stepX, stepY
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull SurfaceSprite create(Identifier identifier, int step) {
        return create(identifier, step, step);
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public IdentifierSprite get(int index) {
        return get(index, index);
    }

    public IdentifierSprite get(int indexX, int indexY) {
        float
                uBegin = (float) (MathHelper.clamp(indexX, 1, stepX) - 1) / this.stepX,
                uEnd = (float) MathHelper.clamp(indexX, 1, stepX) / this.stepX,
                vBegin = (float) (MathHelper.clamp(indexY, 1, stepY) - 1) / this.stepY,
                vEnd = (float) MathHelper.clamp(indexY, 1, stepY) / this.stepY;

        return IdentifierSprite.create(
                identifier,
                uBegin, vBegin, uEnd, vEnd
        );
    }
}
