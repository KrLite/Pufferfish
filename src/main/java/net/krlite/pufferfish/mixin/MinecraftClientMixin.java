package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.pufferfish.PuffKeys.*;
import static net.krlite.pufferfish.util.CrosshairPuffer.crosshairScaleTarget;
import static net.krlite.pufferfish.util.AxisLocker.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    public GameOptions options;

    @Shadow public String fpsDebugString;

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

        PlayerEntity player = MinecraftClient.getInstance().player;
        if ( player != null ) {
            playerUuid = player.getUuid();
            if (LOCK_PITCH.wasPressed() && avaliablePitch) {
                lingerYaw = 0;
                if (lingerPitch == 0) {  // Trigger When Pitch Locked
                    lingerPitch = PuffConfigs.lingerKeyTicks;

                    lockPitch(player);
                    axisLock.replace(Axis.PITCH, !axisLock.get(Axis.PITCH));
                } else {    // Trigger When Pitch Applied
                    lingerPitch = 0;

                    applyPitch(player);
                    axisLock.replace(Axis.PITCH, false);
                    axisPing.replace(Axis.PITCH, true);
                }
            }

            if (LOCK_YAW.wasPressed() && avaliableYaw) {
                lingerPitch = 0;
                if (lingerYaw == 0) {   // Trigger When Yaw Locked
                    lingerYaw = PuffConfigs.lingerKeyTicks;

                    lockYaw(player);
                    axisLock.replace(Axis.YAW, !axisLock.get(Axis.YAW));
                } else {    // Trigger When Yaw Applied
                    lingerYaw = 0;

                    applyYaw(player);
                    axisLock.replace(Axis.YAW, false);
                    axisPing.replace(Axis.YAW, true);
                }
            }
        }

        avaliablePitch = !LOCK_PITCH.isPressed();
        avaliableYaw = !LOCK_YAW.isPressed();
    }
}
