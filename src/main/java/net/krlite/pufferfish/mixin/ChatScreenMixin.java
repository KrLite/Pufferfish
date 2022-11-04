package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.ColoredRenderer;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends DrawableHelper {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    private void render(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        float yOffset = 0.0F;
        MinecraftClient client = MinecraftClient.getInstance();

        if ( PuffConfigs.enableChatAnimation ) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) ChatUtil.chatBackgroundOpacity);
            yOffset = -16.0F;
        }
        ColoredRenderer.fillColoredVertical(
                matrixStack,
                -2, client.currentScreen.height - 16 + yOffset,
                client.currentScreen.width + 2, client.currentScreen.height + 2,
                PuffConfigs.enableChatAnimation
                        ? ColorUtil.castAlpha(ChatUtil.chatBackgroundColor)
                        : ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, (float) client.options.textBackgroundOpacity),
                ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, (float) client.options.textBackgroundOpacity)
        );
    }
}
