package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.AxisLocker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.pufferfish.PuffKeys.*;
import static net.krlite.pufferfish.util.CrosshairPuffer.crosshairScaleTarget;
import static net.krlite.pufferfish.util.ScreenEdgeOverlay.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    public GameOptions options;

    @Shadow @Nullable public Entity targetedEntity;

    @Inject(method = "handleInputEvents", at = @At("TAIL"))
    private void handleInputEvents(CallbackInfo ci) {
        crosshairScaleTarget = options.attackKey.isPressed()
                ? !options.useKey.isPressed()
                        // Attacking
                        ? PuffConfigs.crosshairPuff
                        // Both
                        : PuffConfigs.crosshairOriginal
                : options.useKey.isPressed()
                        // Using
                        ? MathHelper.clamp(2.0 - PuffConfigs.crosshairPuff, 0.3, 1.0)
                        // None
                        : PuffConfigs.crosshairOriginal;

        if ( LOCK_XZ.wasPressed() && avaliableXZ ) {
            lingerY = 0;
            if ( lingerXZ == 0 ) {  // Trigger When XZ Pressed
                lingerXZ = PuffConfigs.lingerKeyTicks;
                AxisLocker.lockAxisXZ = !AxisLocker.lockAxisXZ;

                //MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§7§lPressed " + LOCK_XZ.getBoundKeyTranslationKey()));
            } else {    // Trigger When XZ Double Pressed
                lingerXZ = 0;
                AxisLocker.applyAxisLock(Direction.Axis.X);
                AxisLocker.applyAxisLock(Direction.Axis.Z);
                AxisLocker.lockAxisXZ = false;

                //MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§f§lPressed " + LOCK_XZ.getBoundKeyTranslationKey() + " twice"));
            }
        }

        if ( LOCK_Y.wasPressed() && avaliableY ) {
            lingerXZ = 0;
            if ( lingerY == 0 ) {   // Trigger When Y Pressed
                lingerY = PuffConfigs.lingerKeyTicks;
                AxisLocker.lockAxisY = !AxisLocker.lockAxisY;

                //MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§7§lPressed " + LOCK_Y.getBoundKeyTranslationKey()));
            } else {    // Trigger When Y Double Pressed
                lingerY = 0;
                AxisLocker.applyAxisLock(Direction.Axis.Y);
                AxisLocker.lockAxisY = false;

                //MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§f§lPressed " + LOCK_Y.getBoundKeyTranslationKey() + " twice"));
            }
        }

        avaliableXZ = !LOCK_XZ.isPressed();
        avaliableY = !LOCK_Y.isPressed();
    }
}
