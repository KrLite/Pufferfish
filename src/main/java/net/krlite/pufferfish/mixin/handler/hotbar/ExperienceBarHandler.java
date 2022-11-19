package net.krlite.pufferfish.mixin.handler.hotbar;

import net.krlite.pufferfish.config.Defaults;
import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class ExperienceBarHandler extends DrawableHelper {
    @Shadow public abstract TextRenderer getTextRenderer();

    @Redirect(
            method = "renderExperienceBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void renderExperienceBar(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            y += 23;
        }

        drawTexture(matrixStack, x, y, u, v, width, height);
    }

    @Redirect(
            method = "renderExperienceBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"
            )
    )
    private int renderExperienceLevel(TextRenderer instance, MatrixStack matrixStack, String text, float x, float y, int color) {
        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            y += 23;
        }

        return this.getTextRenderer().draw(matrixStack, text, x, y, color);
    }
}
