package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.ChatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin extends DrawableHelper {
    @Shadow protected abstract boolean isChatFocused();

    @Shadow protected abstract boolean isChatHidden();

    @Shadow @Final private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow private int scrolledLines;

    @Shadow public abstract int getVisibleLineCount();

    @Shadow
    private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }

    private static final List<Integer> messageExistingTicks = new ArrayList<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrices, int tickDelta, CallbackInfo ci) {
        if ( PuffConfigs.enableChatAnimation ) {
            if (!messageExistingTicks.isEmpty()) messageExistingTicks.clear();
            if (!this.isChatHidden() && this.visibleMessages.size() > 0) {
                for (
                        int message = 0;
                        message + this.scrolledLines < this.visibleMessages.size() && message < this.getVisibleLineCount();
                        ++message
                ) {
                    ChatHudLine chatHudLine = this.visibleMessages.get(message + this.scrolledLines);
                    if (chatHudLine != null) {
                        messageExistingTicks.add(message, tickDelta - chatHudLine.getCreationTick());
                    }
                }
            /*
            StringBuilder builder = new StringBuilder();
            for ( Integer index : messageExistingTicks ) {
                builder.append(messageExistingTicks.indexOf(index)).append(" | ").append(index).append(", ");
            }
            PuffMod.LOGGER.warn(builder.toString());
             */
            }
        }
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"
            )
            , slice = @Slice(
                    from = @At("HEAD"),
                    to = @At(value = "INVOKE", target = "Ljava/util/Deque;isEmpty()Z")
            )
    )
    private void renderChat(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        if ( PuffConfigs.enableChatAnimation ) {
            double spacing = 9.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0);
            int message = (int) (-y1 / spacing - 1);
            double opacity = Math.max(
                    (this.isChatFocused() && messageExistingTicks.get(message) >= 200)
                            ? 0.0F
                            : this.getMessageOpacityMultiplier(messageExistingTicks.get(message)),
                    ChatUtil.backgroundOpacity
            );

            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F,
                    (float) opacity
            );
        }
        fill(matrixStack, x1, y1, x2, y2, color);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"
            )
            , slice = @Slice(
                    from = @At(value = "INVOKE", target = "Ljava/util/Deque;isEmpty()Z"),
                    to = @At("TAIL")
            )
    )
    private void renderStaticChat(MatrixStack matrices, int tickDelta, CallbackInfo ci) {
        if ( PuffConfigs.enableChatAnimation ) RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) ChatUtil.backgroundOpacity);
    }
}
