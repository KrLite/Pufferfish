package net.krlite.pufferfish.render.proxied;

import net.krlite.pufferfish.config.PuffConfig;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class ProxiedSubtitlesHud extends DrawableHelper {
    private float widthTarget = 0, heightTarget = 0;
    private float width = widthTarget, height = heightTarget;

    public void render(MatrixStack matrixStack) {
        lerp(0.132F);

        MinecraftClient client = MinecraftClient.getInstance();
        float
                xEnd = client.getWindow().getScaledWidth() + 2,
                yEnd = client.getWindow().getScaledHeight() - 34 + client.textRenderer.fontHeight / 2.0F;

        PuffRenderer.COLORED.fillGradiantHorizontal(
                matrixStack,
                xEnd - width, yEnd - height,
                xEnd, yEnd,
                ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR),
                ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR, (float) (double) client.options.getTextBackgroundOpacity().getValue())
        );
    }

    public void setSize(float width, float height) {
        this.widthTarget = width != 0 ? width : this.widthTarget;
        this.heightTarget = height;
    }

    public void lerp(float delta) {
        this.width = MathHelper.lerp(delta, this.width, this.widthTarget);
        this.height = MathHelper.lerp(delta, this.height, this.heightTarget);
    }
}
