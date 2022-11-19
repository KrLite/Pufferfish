package net.krlite.pufferfish.mixin.actor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static net.krlite.pufferfish.util.AxisUtil.*;

@Mixin(Entity.class)
@Environment(EnvType.CLIENT)
public abstract class PlayerCameraActor {
    @Shadow public abstract UUID getUuid();

    @Inject(method = "getPitch()F", at = @At("RETURN"), cancellable = true)
    public void getPitch(CallbackInfoReturnable<Float> cir) {
        if ( this.getUuid() == playerUuid && axisLock.get(Axis.PITCH) && !axisPing.get(Axis.PITCH) ) {
            cir.setReturnValue(axisStatic.get(Axis.PITCH));
        }
    }

    @Inject(method = "getYaw()F", at = @At("RETURN"), cancellable = true)
    public void getYaw(CallbackInfoReturnable<Float> cir) {
        if ( this.getUuid() == playerUuid && axisLock.get(Axis.YAW) && !axisPing.get(Axis.YAW) ) {
            cir.setReturnValue(axisStatic.get(Axis.YAW));
        }
    }
}
