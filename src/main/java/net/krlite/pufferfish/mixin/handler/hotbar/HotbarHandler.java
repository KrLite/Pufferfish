package net.krlite.pufferfish.mixin.handler.hotbar;

import net.krlite.pufferfish.config.PuffConfig;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.HotBarUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class HotbarHandler {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow protected abstract PlayerEntity getCameraPlayer();
    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack itemStack, int seed);
    @Shadow private @Nullable Text overlayMessage;
    private static int seed = 1;

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0))
    private void HotbarHandler$render(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
        seed = 1;
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            HotBarUtil.updateHotbarOffset(getCameraPlayer());
        }
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ),
            slice = @Slice(
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;")
            )
    )
    private void HotbarHandler$renderHotbarPre(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.push();

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            matrixStack.translate(
                    22,
                    scaledHeight / 2.0F + 14 * HotBarUtil.hotbarOffset,
                    0
            );
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
        } else {
            matrixStack.translate(scaledWidth / 2.0F, scaledHeight, 0);
        }
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    shift = At.Shift.AFTER
            ),
            slice = @Slice(
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;")
            )
    )
    private void HotbarHandler$renderHotbarPost(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At("HEAD"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
            )
    )
    private void HotbarHandler$renderHotbar(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                HotBarUtil.VANILLA_HOTBAR, Color.WHITE,
                matrixStack,
                -91, -22,
                91, 0
        );
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 1
            ),
            slice = @Slice(
                    from = @At("HEAD"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
            )
    )
    private void HotbarHandler$renderSelectedSlot(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        float horizontal = -1 + getCameraPlayer().getInventory().selectedSlot * 20;

        PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                HotBarUtil.VANILLA_SELECTED_SLOT, Color.WHITE,
                matrixStack,
                -91 + horizontal, -23,
                -91 + horizontal + 24, 1
        );
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
                            ordinal = 0
                    )
            )
    )
    private void HotbarHandler$renderOffHandSlotLeft(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                HotBarUtil.VANILLA_OFFHAND_SLOT_LEFT, Color.WHITE,
                matrixStack,
                -91 - 29, -23,
                -91, 1
        );
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 1
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
                            ordinal = 0
                    )
            )
    )
    private void HotbarHandler$renderOffHandSlotRight(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        PuffRenderer.COLORED_TEXTURE.renderColoredTexture(
                HotBarUtil.VANILLA_OFFHAND_SLOT_RIGHT, Color.WHITE,
                matrixStack,
                91, -23,
                91 + 29, 1
        );
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;")
            )
    )
    private void HotbarHandler$renderHotbarAttackIndicator(InGameHud instance, MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            x = scaledWidth - 20;
        }

        PuffRenderer.COLORED_TEXTURE.renderPositionedColoredTexture(
                DrawableHelper.GUI_ICONS_TEXTURE, Color.WHITE,
                matrixStack, x, y, u, v, width, height
        );
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;hsvToRgb(FFF)I")
            )
    )
    private int HotbarHandler$renderOverlayMessage(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            y += 23;
        }

        return textRenderer.drawWithShadow(
                matrixStack, overlayMessage, x, y, color
        );
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
                    ordinal = 0
            )
    )
    private void HotbarHandler$cancelRenderHotBarItem(InGameHud instance, int x, int y, float tickDelta, PlayerEntity player, ItemStack itemStack, int seed) {
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V"
            )
    )
    private void HotbarHandler$renderHotBarItem(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
        for ( int index = 0; index < 9; ++index ) {
            int
                    horizontal = scaledWidth / 2 - 90 + index * 20 + 2,
                    vertical = scaledHeight - 19;

            if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
                vertical = scaledHeight / 2 + 90 - index * 20 - 18 + Math.round(14 * HotBarUtil.hotbarOffset);
                horizontal = 3;
            }

            renderHotbarItem(horizontal, vertical, tickDelta, getCameraPlayer(), getCameraPlayer().getInventory().main.get(index), seed++);
        }
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
                            ordinal = 1
                    )
            )
    )
    private void HotbarHandler$renderOffHandItemLeft(InGameHud instance, int x, int y, float tickDelta, PlayerEntity player, ItemStack itemStack, int seed) {
        int
                horizontal = scaledWidth / 2 - 91 - 26,
                vertical = scaledHeight - 19;

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            vertical = scaledHeight / 2 + 91 + 10 + Math.round(14 * HotBarUtil.hotbarOffset);
            horizontal = 3;
        }

        renderHotbarItem(horizontal, vertical, tickDelta, player, itemStack, seed);
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
                    ordinal = 1
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
                            ordinal = 1
                    )
            )
    )
    private void HotbarHandler$renderOffHandItemRight(InGameHud instance, int x, int y, float tickDelta, PlayerEntity player, ItemStack itemStack, int seed) {
        int
                horizontal = scaledWidth / 2 + 91 + 10,
                vertical = scaledHeight - 19;

        if ( PuffConfig.HOTBAR_POSITION.getValue().isLeft() ) {
            vertical = scaledHeight / 2 - 91 - 26 + Math.round(14 * HotBarUtil.hotbarOffset);
            horizontal = 3;
        }

        renderHotbarItem(horizontal, vertical, tickDelta, player, itemStack, seed);
    }
}
