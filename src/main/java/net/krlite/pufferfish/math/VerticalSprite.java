package net.krlite.pufferfish.math;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VerticalSprite {
    private final Identifier identifier;
    private final int step;

    private VerticalSprite(Identifier identifier, int step) {
        this.identifier = identifier;
        this.step       = step;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull VerticalSprite create(Identifier identifier, int textureWidth, int textureHeight) {
        return create(identifier, textureHeight / textureWidth);
    }

    @Contract("_, _ -> new")
    public static @NotNull VerticalSprite create(Identifier identifier, int step) {
        return new VerticalSprite(identifier, step);
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public IdentifierSprite get(int index) {
        float
                vBegin = (float) (MathHelper.clamp(index, 1, this.step) - 1) / this.step,
                vEnd = (float) MathHelper.clamp(index, 1, this.step) / this.step;

        return IdentifierSprite.create(
                this.identifier,
                0, vBegin, 1, vEnd
        );
    }
}
