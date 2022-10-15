package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.util.AxisLocker;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public class MouseMixin {
    @Redirect(method = "updateMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;mouseSensitivity:D"))
    private double mouseSensitivity(GameOptions options) {
        if ( AxisLocker.axisPing.get(AxisLocker.Axis.PITCH) || AxisLocker.axisPing.get(AxisLocker.Axis.YAW) ) {
            return options.mouseSensitivity * 0.17;
        }

        return options.mouseSensitivity;
    }
}
