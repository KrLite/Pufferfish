package net.krlite.pufferfish.mixin.animator;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.PuffRenderer;
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
public class ChatScreenAnimator extends DrawableHelper {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 0))
    private void render(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        if ( !PuffConfigs.enableChatAnimation ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            PuffRenderer.COLORED.fillColored(
                    matrixStack,
                    -2, MinecraftClient.getInstance().getWindow().getScaledHeight() - 16,
                    MinecraftClient.getInstance().getWindow().getScaledWidth() + 2, MinecraftClient.getInstance().getWindow().getScaledHeight() + 2,
                    ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, MinecraftClient.getInstance().options.getTextBackgroundOpacity().getValue().floatValue())
            );

            RenderSystem.disableBlend();
        }
    }
}
