package net.krlite.pufferfish.mixin.handler.screen_background;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProgressScreen.class)
public class ProgressScreenHandler {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ProgressScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V",
                    ordinal = 0
            )
    )
    private void drawTitle(MatrixStack matrixStack, TextRenderer textRenderer, Text text, int xCentered, int y, int color) {
        ProgressScreen.drawCenteredText(
                matrixStack, textRenderer, text,
                textRenderer.getWidth(text) / 2 + 28,
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 28,
                color
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ProgressScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V",
                    ordinal = 1
            )
    )
    private void drawTask(MatrixStack matrixStack, TextRenderer textRenderer, Text text, int xCentered, int y, int color) {
        ProgressScreen.drawCenteredText(
                matrixStack, textRenderer, text,
                textRenderer.getWidth(text) / 2 + 28,
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 44,
                color
        );
    }
}
