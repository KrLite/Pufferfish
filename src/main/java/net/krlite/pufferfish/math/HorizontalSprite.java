package net.krlite.pufferfish.math;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HorizontalSprite {
    private final Identifier identifier;
    private final int step;

    private HorizontalSprite(Identifier identifier, int step) {
        this.identifier = identifier;
        this.step       = step;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull HorizontalSprite create(Identifier identifier, int textureWidth, int textureHeight) {
        return create(identifier, textureWidth / textureHeight);
    }

    @Contract("_, _ -> new")
    public static @NotNull HorizontalSprite create(Identifier identifier, int step) {
        return new HorizontalSprite(identifier, step);
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public IdentifierSprite get(int index) {
        float
                uBegin = (float) (MathHelper.clamp(index, 1, this.step)  - 1) / this.step,
                uEnd = (float) MathHelper.clamp(index, 1, this.step) / this.step;

        return IdentifierSprite.create(
                this.identifier,
                uBegin, 0, uEnd, 1
        );
    }
}
