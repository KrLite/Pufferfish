package net.krlite.pufferfish.mixin.animator;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.core.Broadcaster;
import net.krlite.pufferfish.core.IHashable;
import net.krlite.pufferfish.math.Timer;
import net.krlite.pufferfish.math.solver.EasingFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
class TitleScreenTrigger implements Broadcaster.IBroadcaster, IHashable {
    private final int titleTrigger = hash(TitleScreen.class, "TitleTrigger");

    @Inject(method = "setScreen", at = @At("TAIL"))
    private void MinecraftClientTrigger$titleTrigger(Screen screen, CallbackInfo ci) {
        if (!(screen instanceof TitleScreen) && PuffConfigs.enableTitleAnimation) {
            broadcast(titleTrigger, false);
        }
    }
}

@Mixin(TitleScreen.class)
public class TitleScreenAnimator implements Broadcaster.IBroadcaster, IHashable {
    private Timer titleTimer = new Timer();
    private double titlePos = 0;
    private final int titleTrigger = hash(TitleScreen.class, "TitleTrigger");

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;MINECRAFT_TITLE_TEXTURE:Lnet/minecraft/util/Identifier;")
            )
    )
    private void TitleScreenAnimator$titleTrigger(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);

        if ( alpha > 0 && PuffConfigs.enableTitleAnimation && !(boolean) getBroadcastOrDefault(titleTrigger, false) ) {
            titleTimer = new Timer();
            broadcast(titleTrigger, true);
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;MINECRAFT_TITLE_TEXTURE:Lnet/minecraft/util/Identifier;",
                    shift = At.Shift.AFTER
            )
    )
    private void TitleScreenAnimator$render(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        double offset = PuffConfigs.enableTitleAnimation
                ? MinecraftClient.getInstance().getWindow().getScaledHeight() / 3.5 : 0;

        if ( (boolean) getBroadcastOrDefault(titleTrigger, false) ) {
            double titleFadeMs = 853;
            double time = titleTimer.queue() > titleFadeMs ? titleFadeMs : titleTimer.queue();
            titlePos = EasingFunctions.easeOutBounce(time, -offset, offset, titleFadeMs);
        } else {
            titlePos = -offset;
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void TitleScreenAnimator$minecraftTitlePre(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.push();
        matrixStack.translate(0, titlePos, 0);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void TitleScreenAnimator$minecraftTitlePost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void TitleScreenAnimator$editionPre(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.push();
        matrixStack.translate(0, titlePos, 0);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void TitleScreenAnimator$editionPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen$DeprecationNotice;render(Lnet/minecraft/client/util/math/MatrixStack;I)V")
            )
    )
    private void TitleScreenAnimator$splashText(MatrixStack matrixStack, double x, double y, double z) {
        matrixStack.translate(x, titlePos + 70, z);
    }
}