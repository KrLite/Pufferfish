package net.krlite.pufferfish.render.proxied;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.equator.core.MatrixWrapper;
import net.krlite.equator.render.Equator;
import net.krlite.pufferfish.config.PuffConfig;
import net.krlite.pufferfish.core.Broadcaster;
import net.krlite.pufferfish.core.IHashable;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;

public class ProxiedChatScreen extends DrawableHelper implements Broadcaster.IBroadcaster, IHashable {
    private final int chatBackgroundOpacity = hash(ChatScreen.class, "ChatBackgroundOpacity");

    public void render(MatrixStack matrixStack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) (double) getBroadcastOrDefault(chatBackgroundOpacity, 0.0));

        MinecraftClient client = MinecraftClient.getInstance();
        float yOffset = -32.0F * (float) (double) getBroadcastOrDefault(chatBackgroundOpacity, 0.0);

        Equator.Colors.gradientVertical(
                new MatrixWrapper(
                        matrixStack,
                        -2, client.getWindow().getScaledHeight() + yOffset,
                        client.getWindow().getScaledWidth() + 2, client.getWindow().getScaledHeight() + 2
                ),
                ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue()),
                ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue(), client.options.getTextBackgroundOpacity().getValue().floatValue())
        );
    }
}
