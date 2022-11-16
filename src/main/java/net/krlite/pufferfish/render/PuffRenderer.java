package net.krlite.pufferfish.render;

import net.krlite.pufferfish.render.base.ColoredRenderer;
import net.krlite.pufferfish.render.base.ColoredTextureRenderer;
import net.krlite.pufferfish.render.base.MaskedTextureRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class PuffRenderer {
    public static final MatrixStack extraBefore = new MatrixStack();
    public static final MatrixStack extraInGame = new MatrixStack();
    public static final MatrixStack extraAfter = new MatrixStack();

    public static final ColoredTextureRenderer COLORED_TEXTURE = new ColoredTextureRenderer();
    public static final MaskedTextureRenderer MASKED_TEXTURE = new MaskedTextureRenderer();
    public static final ColoredRenderer COLORED = new ColoredRenderer();

    public static void init() {

    }
}
