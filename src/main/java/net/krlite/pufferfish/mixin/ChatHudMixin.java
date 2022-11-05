package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
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
public abstract class ChatHudMixin extends DrawableHelper{
    @Shadow protected abstract boolean isChatFocused();

    @Shadow @Final private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow private int scrolledLines;

    @Shadow public abstract int getVisibleLineCount();

    @Shadow
    private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }

    private static final List<Double> messageExistingTicks = new ArrayList<>();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;getChatScale()D"))
    private void render(MatrixStack matrixStack, int tickDelta, CallbackInfo ci) {
        if ( PuffConfigs.enableChatAnimation ) {
            if (!messageExistingTicks.isEmpty()) messageExistingTicks.clear();
            for (
                    int message = 0;
                    message + this.scrolledLines < this.visibleMessages.size() && message < this.getVisibleLineCount();
                    ++message
            ) {
                ChatHudLine<OrderedText> chatHudLine = this.visibleMessages.get(message + this.scrolledLines);
                if (chatHudLine != null) {
                    messageExistingTicks.add(message, (double) (tickDelta - chatHudLine.getCreationTick()));
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
                    to = @At(value = "INVOKE", target = "Ljava/util/Deque;isEmpty()Z")
            )
    )
    private void renderChatBackground(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        RenderSystem.enableBlend();
        float opacity = 1.0F;

        if ( PuffConfigs.enableChatAnimation ) {
            double spacing = 9.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0);
            int message = (-yBegin / (int) spacing - 1), existingTicks = (int) Math.round(messageExistingTicks.get(message));
            opacity = (float) Math.max(
                    existingTicks >= 200
                            ? 0.0F
                            : existingTicks > 10
                                    ? getMessageOpacityMultiplier(message)
                                    : Math.pow(existingTicks / 10.0, 2.0),
                    this.isChatFocused()
                            ? ChatUtil.chatBackgroundOpacity
                            : 0.0F
            );

            messageExistingTicks.set(message, existingTicks + opacity / 10.0);
            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F,
                    opacity
            );
        }

        PuffMod.CR.fillGradiantHorizontal(
                matrixStack,
                xBegin, yBegin,
                xEnd * opacity, yEnd,
                ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, color),
                PuffConfigs.enableChatAnimation
                        ? ColorUtil.castAlpha(ChatUtil.chatBackgroundColor)
                        : ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, color)
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"
            )
    )
    private int renderChat(TextRenderer instance, MatrixStack matrixStack, OrderedText text, float x, float y, int color) {
        RenderSystem.enableBlend();
        float opacity = 1.0F, xOffset = 0.0F, yOffset = 0.0F;

        if ( PuffConfigs.enableChatAnimation ) {
            double
                    spacing = 9.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0),
                    spacingAlt = -8.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0) + 4.0 * MinecraftClient.getInstance().options.chatLineSpacing;
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

        return drawText(
                matrixStack, text,
                x + xOffset, y + yOffset,
                opacity < 1.0
                        ? ColorUtil.castAlpha(
                                ChatUtil.chatTextColor, (float) (opacity * MinecraftClient.getInstance().options.chatOpacity)
                        ).getRGB()
                        : ColorUtil.castAlpha(ChatUtil.chatTextColor, color).getRGB(),
                PuffConfigs.enableChatTextShadow
        );
    }

    @Redirect(
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
    private void renderStaticChatBackground(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        RenderSystem.enableBlend();
        if ( PuffConfigs.enableChatAnimation ) {
            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F,
                    (float) ChatUtil.chatBackgroundOpacity
            );
        }
        PuffMod.CR.fillGradiantHorizontal(
                matrixStack,
                xBegin, yBegin,
                xEnd,   yEnd,
                ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, color),
                PuffConfigs.enableChatAnimation
                        ? ColorUtil.castAlpha(ChatUtil.chatBackgroundColor)
                        : ColorUtil.castAlpha(ChatUtil.chatBackgroundColor, color)
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
            )
    )
    private int renderStaticChat(TextRenderer instance, MatrixStack matrixStack, Text text, float x, float y, int color) {
        RenderSystem.enableBlend();
        return drawText(
                matrixStack, text,
                x, y,
                ColorUtil.castAlpha(
                        ChatUtil.chatTextColor,
                        PuffConfigs.enableChatAnimation
                                ? (float) (ChatUtil.chatBackgroundOpacity * MinecraftClient.getInstance().options.chatOpacity)
                                : (float) (color >> 24 & 255) / 255.0F
                ).getRGB(),
                PuffConfigs.enableChatTextShadow
        );
    }
}
