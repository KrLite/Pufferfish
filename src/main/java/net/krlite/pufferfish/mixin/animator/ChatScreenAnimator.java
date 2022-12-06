package net.krlite.pufferfish.mixin.animator;

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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
class ChatScreenTrigger implements Broadcaster.IBroadcaster, IHashable {
    @Shadow @Nullable public Screen currentScreen;
    private final int chatBackgroundOpacityTarget = hash(ChatScreen.class, "ChatBackgroundOpacityTarget");

    @Inject(method = "tick", at = @At("TAIL"))
    private void ChatScreenTrigger$chatBackgroundOpacityTrigger(CallbackInfo ci) {
        if ( currentScreen instanceof ChatScreen ) {
            broadcast(chatBackgroundOpacityTarget, 1.0);
        } else {
            broadcast(chatBackgroundOpacityTarget, 0.0);
        }
    }
}

@Mixin(ChatScreen.class)
public class ChatScreenAnimator extends DrawableHelper {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 0))
    private void ChatScreenAnimator$render(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        if ( !PuffConfig.ENABLE_CHAT_ANIMATION.getValue() ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Equator.Colors.fill(
                    new MatrixWrapper(
                            matrixStack,
                            -2, MinecraftClient.getInstance().getWindow().getScaledHeight() - 16,
                            MinecraftClient.getInstance().getWindow().getScaledWidth() + 2, MinecraftClient.getInstance().getWindow().getScaledHeight() + 2
                    ), ColorUtil.castAlpha(
                            PuffConfig.CHAT_BACKGROUND_COLOR.getValue(),
                            MinecraftClient.getInstance().options.getTextBackgroundOpacity().getValue().floatValue()
                    )
            );

            RenderSystem.disableBlend();
        }
    }
}
