package net.krlite.pufferfish.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenHandler {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"
            )
    )
    private void drawTitle(MatrixStack matrixStack, TextRenderer textRenderer, Text text, int xCentered, int y, int color) {
        DownloadingTerrainScreen.drawCenteredText(
                matrixStack, textRenderer, text,
                textRenderer.getWidth(text) / 2 + 28,
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 28,
                color
        );
    }
}
