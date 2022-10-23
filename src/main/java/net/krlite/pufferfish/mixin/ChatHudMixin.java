package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.ColoredRenderer;
import net.krlite.pufferfish.util.ChatUtil;
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

import java.awt.*;
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

    private static final List<Double> messageExistingTicks = new ArrayList<>();

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
        float yOffset = 0.0F;
        if ( PuffConfigs.enableChatAnimation ) {
            double spacing = 9.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0);
            int message = (int) (-yBegin / spacing - 1), existingTicks = (int) Math.round(messageExistingTicks.get(message));
            float opacity = (float) Math.max(
                    this.isChatFocused()
                            ? existingTicks >= 200
                                    ? 0.0F
                                    : existingTicks > 10
                                            ? getMessageOpacityMultiplier(message)
                                            : Math.pow(existingTicks / 10.0, 2.0)
                            : existingTicks > 10
                                    ? getMessageOpacityMultiplier(message)
                                    : Math.pow(existingTicks / 10.0, 2.0),
                    this.isChatFocused()
                            ? ChatUtil.backgroundOpacity
                            : 0.0F
            );

            yOffset =
                    existingTicks <= 10
                            ? (float) Math.pow(1 - existingTicks / 10.0, 2.0) * 23.5F
                            : 0.0F;

            messageExistingTicks.set(message, existingTicks + opacity / 10.0);

            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F,
                    opacity
            );
        }

        ColoredRenderer.fill(
                matrixStack,
                xBegin, yBegin + yOffset,
                xEnd,   yEnd + yOffset,
                new Color(color, true)
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
        if ( PuffConfigs.enableChatAnimation ) {
            double
                    spacing = 9.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0),
                    spacingAlt = -8.0 * (MinecraftClient.getInstance().options.chatLineSpacing + 1.0) + 4.0 * MinecraftClient.getInstance().options.chatLineSpacing;
            int message = (int) ((spacingAlt - y) / spacing), existingTicks = (int) Math.round(messageExistingTicks.get(message));
            float opacity = MathHelper.clamp((float) ((messageExistingTicks.get(message) % 1.0F) * 10.0F), 0.1F, 1.0F);

            float yOffset =
                    existingTicks <= 10
                            ? (float) Math.pow(1 - existingTicks / 10.0, 2.0) * 23.5F
                            : 0.0F;

            MinecraftClient.getInstance().textRenderer.drawWithShadow(
                    matrixStack, text, x, y + yOffset,
                    opacity < 1.0F
                            ? new Color(
                                    ChatUtil.chatTextColor.getRed() / 255.0F,
                                    ChatUtil.chatTextColor.getGreen() / 255.0F,
                                    ChatUtil.chatTextColor.getBlue() / 255.0F,
                                    opacity
                            ).getRGB()
                            : new Color(
                                    ChatUtil.chatTextColor.getRed() / 255.0F,
                                    ChatUtil.chatTextColor.getGreen() / 255.0F,
                                    ChatUtil.chatTextColor.getBlue() / 255.0F,
                                    (float) (color >> 24 & 255) / 255.0F
                            ).getRGB()
            );
        } else {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(
                    matrixStack, text, x, y,
                    new Color(
                            ChatUtil.chatTextColor.getRed() / 255.0F,
                            ChatUtil.chatTextColor.getGreen() / 255.0F,
                            ChatUtil.chatTextColor.getBlue() / 255.0F,
                            (float) (color >> 24 & 255) / 255.0F
                    ).getRGB()
            );
        }

        return 0;
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
    private void renderStaticChatBackground(MatrixStack matrices, int tickDelta, CallbackInfo ci) {
        if ( PuffConfigs.enableChatAnimation ) RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) ChatUtil.backgroundOpacity);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
            )
    )
    private int renderStaticChat(TextRenderer instance, MatrixStack matrixStack, Text text, float x, float y, int color) {
        if ( PuffConfigs.enableChatAnimation ) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(
                    matrixStack, text, x, y,
                    new Color(
                            ChatUtil.chatTextColor.getRed() / 255.0F,
                            ChatUtil.chatTextColor.getGreen() / 255.0F,
                            ChatUtil.chatTextColor.getBlue() / 255.0F,
                            (float) ChatUtil.backgroundOpacity
                    ).getRGB()
            );
        } else {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(
                    matrixStack, text, x, y,
                    new Color(
                            ChatUtil.chatTextColor.getRed() / 255.0F,
                            ChatUtil.chatTextColor.getGreen() / 255.0F,
                            ChatUtil.chatTextColor.getBlue() / 255.0F,
                            (float) (color >> 24 & 255) / 255.0F
                    ).getRGB()
            );
        }

        return  0;
    }
}
