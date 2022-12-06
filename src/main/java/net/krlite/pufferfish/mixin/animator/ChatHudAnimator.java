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
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
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
public abstract class ChatHudAnimator extends DrawableHelper implements Broadcaster.IBroadcaster, IHashable {
    @Shadow protected abstract boolean isChatFocused();
    @Shadow private int scrolledLines;
    @Shadow public abstract int getVisibleLineCount();
    @Shadow private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    private final List<Double> messageExistingTicks = new ArrayList<>();
    private final int chatBackgroundOpacityTarget = hash(ChatScreen.class, "ChatBackgroundOpacityTarget");
    private final int chatBackgroundOpacity = hash(ChatScreen.class, "ChatBackgroundOpacity");

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;getChatScale()D"))
    private void ChatHudAnimator$render(MatrixStack matrixStack, int tickDelta, CallbackInfo ci) {
        if ( PuffConfig.ENABLE_CHAT_ANIMATION.getValue() ) {
            broadcast(
                    chatBackgroundOpacity,
                    MathHelper.lerp(
                            0.107,
                            (double) getBroadcastOrDefault(chatBackgroundOpacity, 0.0),
                            (double) getBroadcast(chatBackgroundOpacityTarget)
                    )
            );

            if ( !messageExistingTicks.isEmpty() ) messageExistingTicks.clear();
            for (
                    int message = 0;
                    message + this.scrolledLines < this.visibleMessages.size() && message < this.getVisibleLineCount();
                    ++message
            ) {
                ChatHudLine.Visible visible = this.visibleMessages.get(message + this.scrolledLines);
                if (visible != null) {
                    messageExistingTicks.add(message, (double) (tickDelta - visible.addedTime()));
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

    private int drawText(MatrixStack matrixStack, Text text, float x, float y, int color, boolean shadow) {
        return shadow
                ? MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, text, x, y, color)
                : MinecraftClient.getInstance().textRenderer.draw(matrixStack, text, x, y, color);
    }

    private int drawText(MatrixStack matrixStack, OrderedText text, float x, float y, int color, boolean shadow) {
        return shadow
                ? MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, text, x, y, color)
                : MinecraftClient.getInstance().textRenderer.draw(matrixStack, text, x, y, color);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"
            )
            , slice = @Slice(
                    from = @At("HEAD"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;indicator()Lnet/minecraft/client/gui/hud/MessageIndicator;")
            )
    )
    private void ChatHudAnimator$renderChatBackground(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        RenderSystem.enableBlend();
        float opacity = 1.0F;

        if ( PuffConfig.ENABLE_CHAT_ANIMATION.getValue() ) {
            double spacing = 9.0 * (MinecraftClient.getInstance().options.getChatLineSpacing().getValue() + 1.0);
            int message = (-yBegin / (int) spacing - 1), existingTicks = (int) Math.round(messageExistingTicks.get(message));
            opacity = (float) Math.max(
                    existingTicks >= 200
                            ? 0.0F
                            : existingTicks > 10
                                    ? getMessageOpacityMultiplier(message)
                                    : Math.pow(existingTicks / 10.0, 2.0),
                    this.isChatFocused()
                            ? (double) getBroadcast(chatBackgroundOpacity)
                            : 0.0F
            );

            messageExistingTicks.set(message, existingTicks + opacity / 10.0);
            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F,
                    opacity
            );
        }

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            xEnd += 25;
        }

        Equator.Colors.gradientHorizontal(
                new MatrixWrapper(
                        matrixStack,
                        xBegin, yBegin,
                        xEnd * opacity, yEnd
                ),
                ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue(), color),
                PuffConfig.ENABLE_CHAT_ANIMATION.getValue()
                        ? ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue())
                        : ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue(), color)
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"
            )
    )
    private int ChatHudAnimator$renderChat(TextRenderer instance, MatrixStack matrixStack, OrderedText text, float x, float y, int color) {
        RenderSystem.enableBlend();

        float opacity = 1.0F, xOffset = 0.0F, yOffset = 0.0F;

        if ( PuffConfig.ENABLE_CHAT_ANIMATION.getValue() ) {
            double
                    spacing = 9.0 * (MinecraftClient.getInstance().options.getChatLineSpacing().getValue() + 1.0),
                    spacingAlt = -8.0 * (MinecraftClient.getInstance().options.getChatLineSpacing().getValue() + 1.0) + 4.0 * MinecraftClient.getInstance().options.getChatLineSpacing().getValue();
            int message = (int) (((int) spacingAlt - y) / (int) spacing), existingTicks = (int) Math.round(messageExistingTicks.get(message));
            float offset =
                    existingTicks <= 15
                            ? (float) Math.pow(1 - existingTicks / 15.0, 5.0)
                            : 0.0F;

            opacity = MathHelper.clamp((float) ((messageExistingTicks.get(message) % 1.0F) * 10.0F), 0.1F, 1.0F);
            xOffset = -offset * (
                    MinecraftClient.getInstance().textRenderer.getWidth(
                            MinecraftClient.getInstance().player.getName()
                    ) + 2
            );
            yOffset = offset * 9.0F;
        }

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            xOffset += 25;
        }

        return drawText(
                matrixStack, text,
                x + xOffset, y + yOffset,
                opacity < 1.0
                        ? ColorUtil.castAlpha(
                                PuffConfig.CHAT_TEXT_COLOR.getValue(), (float) (opacity * MinecraftClient.getInstance().options.getChatOpacity().getValue())
                        ).getRGB()
                        : ColorUtil.castAlpha(PuffConfig.CHAT_TEXT_COLOR.getValue(), color).getRGB(),
                PuffConfig.ENABLE_CHAT_TEXT_SHADOW.getValue()
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"
            )
            , slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;getUnprocessedMessageCount()J"),
                    to = @At("TAIL")
            )
    )
    private void ChatHudAnimator$renderStaticChatBackground(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        RenderSystem.enableBlend();

        if ( PuffConfig.ENABLE_CHAT_ANIMATION.getValue() ) {
            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F,
                    (float) (double) getBroadcast(chatBackgroundOpacity)
            );
        }

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            xEnd += 25;
        }

        Equator.Colors.gradientHorizontal(
                new MatrixWrapper(
                        matrixStack,
                        xBegin, yBegin,
                        xEnd,   yEnd
                ),
                ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue(), color),
                PuffConfig.ENABLE_CHAT_ANIMATION.getValue()
                        ? ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue())
                        : ColorUtil.castAlpha(PuffConfig.CHAT_BACKGROUND_COLOR.getValue(), color)
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
            )
    )
    private int ChatHudAnimator$renderStaticChat(TextRenderer instance, MatrixStack matrixStack, Text text, float x, float y, int color) {
        RenderSystem.enableBlend();

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            x += 25;
        }

        return drawText(
                matrixStack, text,
                x, y,
                ColorUtil.castAlpha(
                        PuffConfig.CHAT_TEXT_COLOR.getValue(),
                        PuffConfig.ENABLE_CHAT_ANIMATION.getValue()
                                ? (float) Math.pow((double) getBroadcast(chatBackgroundOpacity) * MinecraftClient.getInstance().options.getChatOpacity().getValue(), 2.0)
                                : (float) (color >> 24 & 255) / 255.0F
                ).getRGB(),
                PuffConfig.ENABLE_CHAT_TEXT_SHADOW.getValue()
        );
    }
}
