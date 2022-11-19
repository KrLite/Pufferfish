package net.krlite.pufferfish.render.proxied;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class ProxiedChatScreen extends DrawableHelper {
    public void render(MatrixStack matrixStack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) ChatUtil.chatBackgroundOpacity);

        MinecraftClient client = MinecraftClient.getInstance();
        float yOffset = -32.0F * (float) ChatUtil.chatBackgroundOpacity;

        PuffRenderer.COLORED.fillGradiantVertical(
                matrixStack,
                -2, client.getWindow().getScaledHeight() + yOffset,
                client.getWindow().getScaledWidth() + 2, client.getWindow().getScaledHeight() + 2,
                ColorUtil.castAlpha(ChatUtil.chatBackgroundColor),
                ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, (float) (double) client.options.getTextBackgroundOpacity().getValue())
        );
    }
}
