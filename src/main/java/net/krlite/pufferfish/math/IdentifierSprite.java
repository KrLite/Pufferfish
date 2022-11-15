package net.krlite.pufferfish.math;

import net.minecraft.util.Identifier;

public class IdentifierSprite {
    private final Identifier identifier;
    private final float uBegin, vBegin, uEnd, vEnd;

    private IdentifierSprite(Identifier identifier, float uBegin, float vBegin, float uEnd, float vEnd) {
        this.identifier = identifier;
        this.uBegin     = uBegin;
        this.vBegin     = vBegin;
        this.uEnd       = uEnd;
        this.vEnd       = vEnd;
    }

    public static IdentifierSprite of(Identifier identifier) {
        return new IdentifierSprite(identifier, 0, 0, 1, 1);
    }

    public static IdentifierSprite create(Identifier identifier, float uBegin, float vBegin, float uEnd, float vEnd) {
        return new IdentifierSprite(
                identifier,
                uBegin, vBegin,
                uEnd,   vEnd
        );
    }

    public static IdentifierSprite create(Identifier identifier, int textureSize) {
        return create(identifier, textureSize, textureSize, 0, 0, textureSize, textureSize);
    }

    public static IdentifierSprite create(Identifier identifier, int textureWidth, int textureHeight) {
        return create(identifier, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    public static IdentifierSprite create(Identifier identifier, int textureSize, int x, int y, int width, int height) {
        return create(identifier, textureSize, textureSize, x, y, width, height);
    }

    public static IdentifierSprite create(Identifier identifier, int textureWidth, int textureHeight, int x, int y, int width, int height) {
        return new IdentifierSprite(
                identifier,
                (float) x / textureWidth, (float) y / textureHeight,
                (float) (x + width) / textureWidth, (float) (y + height) / textureHeight
        );
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public float uBegin() {
        return this.uBegin;
    }

    public float vBegin() {
        return this.vBegin;
    }

    public float uEnd() {
        return this.uEnd;
    }

    public float vEnd() {
        return this.vEnd;
    }
}
