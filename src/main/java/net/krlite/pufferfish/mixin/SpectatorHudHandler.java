package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.HotBarUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.gui.hud.spectator.SpectatorMenuCommand;
import net.minecraft.client.gui.hud.spectator.SpectatorMenuState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(SpectatorHud.class)
public abstract class SpectatorHudHandler {
    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract float getSpectatorMenuHeight();

    private float getSpectatorMenuHeightAntiAliased(int height) {
        return (1 - this.getSpectatorMenuHeight() * height % 1) % 1;
    }

    @Inject(
            method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SpectatorHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    private void pushMatrixStack(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
        matrixStack.push();
        int
                scaledWidth = this.client.getWindow().getScaledWidth(),
                scaledHeight = this.client.getWindow().getScaledHeight();

        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            matrixStack.translate(
                    22.0F * getSpectatorMenuHeight(),
                    scaledHeight / 2.0F,
                    0
            );
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
        } else {
            matrixStack.translate(scaledWidth / 2.0F, scaledHeight + 22 - 22.0F * getSpectatorMenuHeight(), 0);
        }
    }

    @Inject(
            method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SpectatorHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.AFTER
            )
    )
    private void popMatrixStack(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Redirect(
            method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SpectatorHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 0
            )
    )
    private void renderHotbar(SpectatorHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                HotBarUtil.VANILLA_HOTBAR, Color.WHITE,
                matrixStack,
                -91, -22,
                91, 0
        );
    }

    @Redirect(
            method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SpectatorHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 1
            )
    )
    private void renderSelectedSlot(SpectatorHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        int horizontal = x + 92 - Math.round(this.client.getWindow().getScaledWidth() / 2.0F);

        PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                HotBarUtil.VANILLA_SELECTED_SLOT, Color.WHITE,
                matrixStack,
                -91 + horizontal, -23,
                -91 + horizontal + 24, 1
        );
    }

    @Redirect(
            method = "renderSpectatorCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
            )
    )
    private int renderSpectatorCommandText(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            matrixStack.push();
            matrixStack.translate(x, y, 0);
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));

            int result = textRenderer.drawWithShadow(
                    matrixStack, text,
                    textRenderer.getWidth(text) / 2.0F - 1, 20, color
            );

            matrixStack.pop();
            return result;
        } else {
            return textRenderer.drawWithShadow(matrixStack, text, x, y, color);
        }
    }

    @Inject(
            method = "renderSpectatorCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/spectator/SpectatorMenuCommand;renderIcon(Lnet/minecraft/client/util/math/MatrixStack;FI)V"
            )
    )
    public void pushIconMatrixStack(MatrixStack matrixStack, int slot, int x, float y, float height, SpectatorMenuCommand command, CallbackInfo ci) {
        matrixStack.push();

        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
        }
    }

    @Inject(
            method = "renderSpectatorCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/spectator/SpectatorMenuCommand;renderIcon(Lnet/minecraft/client/util/math/MatrixStack;FI)V",
                    shift = At.Shift.AFTER
            )
    )
    public void popIconMatrixStack(MatrixStack matrixStack, int slot, int x, float y, float height, SpectatorMenuCommand command, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Inject(
            method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SpectatorHud;renderSpectatorCommand(Lnet/minecraft/client/util/math/MatrixStack;IIFFLnet/minecraft/client/gui/hud/spectator/SpectatorMenuCommand;)V"
            )
    )
    private void pushCommandMatrixStack(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
        matrixStack.push();
        int
                scaledWidth = this.client.getWindow().getScaledWidth(),
                scaledHeight = this.client.getWindow().getScaledHeight();

        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            matrixStack.translate(
                    44 * getSpectatorMenuHeight() - scaledHeight - 22 + getSpectatorMenuHeightAntiAliased(22),
                    Math.floor((scaledWidth + scaledHeight) / 2.0) - 16,
                    0
            );
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
        } else {
            matrixStack.translate(0, getSpectatorMenuHeightAntiAliased(22), 0);
        }
    }

    @Inject(
            method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SpectatorHud;renderSpectatorCommand(Lnet/minecraft/client/util/math/MatrixStack;IIFFLnet/minecraft/client/gui/hud/spectator/SpectatorMenuCommand;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void popCommandMatrixStack(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
            )
    )
    private int renderText(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
        if ( PuffConfigs.hotbarPosition.isLeft() ) {
            y += 23;
        }

        return textRenderer.drawWithShadow(matrixStack, text, x, y, color);
    }
}
