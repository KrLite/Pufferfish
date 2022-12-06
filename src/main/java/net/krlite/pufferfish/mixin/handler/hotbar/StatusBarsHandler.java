package net.krlite.pufferfish.mixin.handler.hotbar;

import net.krlite.pufferfish.config.PuffConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class StatusBarsHandler extends DrawableHelper {
    @Shadow protected abstract void renderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking);

    @Shadow public abstract TextRenderer getTextRenderer();

    // Mount Health
    @Redirect(
            method = "renderMountHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void renderMountHealth(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            y += 23;
        }

        drawTexture(matrixStack, x, y, u, v, width, height);
    }

    // Mount Jump Bar
    @Redirect(
            method = "renderMountJumpBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void renderMountJumpBar(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            y += 23;
        }

        drawTexture(matrixStack, x, y, u, v, width, height);
    }

    // Armor Bar, Food Bar and Air Bar
    @Redirect(
            method = "renderStatusBars",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void renderStatusBars(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            y += 23;
        }

        drawTexture(matrixStack, x, y, u, v, width, height);
    }

    // Health Bar
    @Redirect(
            method = "renderStatusBars",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"
            )
    )
    private void renderHealthBar(InGameHud instance, MatrixStack matrixStack, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            y += 23;
        }

        renderHealthBar(matrixStack, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
    }
    
    // Held Item Tooltip
    @Redirect(
            method = "renderHeldItemTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
            )
    )
    private int renderHeldItemTooltip(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            y += 23;
        }

        return textRenderer.drawWithShadow(matrixStack, text, x, y, color);
    }

    @Redirect(
            method = "renderHeldItemTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"
            )
    )
    private void fillHeldItemTooltip(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            yBegin += 23;
            yEnd += 23;
        }

        fill(matrixStack, xBegin, yBegin, xEnd, yEnd, color);
    }
}
