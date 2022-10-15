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
            if ( FLIP_PREFIX.isPressed() ) {
                if ( LOCK_PITCH.wasPressed() && Math.abs(player.getPitch()) >= 0.5F ) {
                    applyPitch(-player.getPitch());
                }

                if ( LOCK_YAW.wasPressed() ) {
                    applyYaw(player.getYaw() + 90.0F);
                }
            }

            else {
                if ( LOCK_PITCH.wasPressed() && availableKeyAxis.get(Axis.PITCH) ) {
                    lingerKeyAxis.replace(Axis.YAW, 0);

                    if ( lingerKeyAxis.get(Axis.PITCH) == 0 ) {  // Trigger When Pitch Locked
                        lingerKeyAxis.replace(Axis.PITCH, PuffConfigs.lingerKeyTicks);

                        lockPitch(player);
                        axisLock.replace(Axis.PITCH, !axisLock.get(Axis.PITCH));
                    }

                    else {    // Trigger When Pitch Applied
                        lingerKeyAxis.replace(Axis.PITCH, 0);

                        applyPitch(player);
                        axisLock.replace(Axis.PITCH, false);
                    }
                }

                if ( LOCK_YAW.wasPressed() && availableKeyAxis.get(Axis.YAW) ) {
                    lingerKeyAxis.replace(Axis.PITCH, 0);

                    if ( lingerKeyAxis.get(Axis.YAW) == 0 ) {   // Trigger When Yaw Locked
                        lingerKeyAxis.replace(Axis.YAW, PuffConfigs.lingerKeyTicks);

                        lockYaw(player);
                        axisLock.replace(Axis.YAW, !axisLock.get(Axis.YAW));
                    }

                    else {    // Trigger When Yaw Applied
                        lingerKeyAxis.replace(Axis.YAW, 0);

                        applyYaw(player);
                        axisLock.replace(Axis.YAW, false);
                    }
                }
            }
        }

        availableKeyAxis.replace(Axis.PITCH, !LOCK_PITCH.isPressed());
        availableKeyAxis.replace(Axis.YAW, !LOCK_YAW.isPressed());
    }
}
