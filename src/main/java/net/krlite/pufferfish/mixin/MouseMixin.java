package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.util.AxisLocker;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Mouse.class)
public class MouseMixin {
    @Redirect(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"
            ),
            slice = @Slice(
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/option/GameOptions;getMouseSensitivity()Lnet/minecraft/client/option/SimpleOption;",
                            shift = At.Shift.AFTER
                    )
            )
    )
    private Object mouseSensitivity(SimpleOption<Double> mouseSensitivity) {
        if ( AxisLocker.axisPing.get(AxisLocker.Axis.PITCH) || AxisLocker.axisPing.get(AxisLocker.Axis.YAW) ) {
            return mouseSensitivity.getValue() * 0.17;
        }

        return mouseSensitivity.getValue();
    }
}
