package net.krlite.pufferfish.render.delayed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class DelayedChatScreen extends DrawableHelper {
    public void render(MatrixStack matrixStack) {
        MinecraftClient client = MinecraftClient.getInstance();

        float yOffset = -16.0F;

        if (PuffConfigs.enableChatAnimation) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) ChatUtil.chatBackgroundOpacity);
            yOffset = -32.0F * (float) ChatUtil.chatBackgroundOpacity;
        }

        PuffRenderer.COLORED.fillGradiantVertical(
                matrixStack,
                -2, client.getWindow().getScaledHeight() + yOffset,
                client.getWindow().getScaledWidth() + 2, client.getWindow().getScaledHeight() + 2,
                PuffConfigs.enableChatAnimation
                        ? ColorUtil.castAlpha(ChatUtil.chatBackgroundColor)
                        : ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, (float) (double) client.options.getTextBackgroundOpacity().getValue()),
                ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, (float) (double) client.options.getTextBackgroundOpacity().getValue())
        );
    }
}
