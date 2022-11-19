package net.krlite.pufferfish.mixin.actor;

import net.krlite.pufferfish.util.AxisUtil;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Mouse.class)
public class MouseSensitivityActor {
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
        if ( AxisUtil.axisPing.get(AxisUtil.Axis.PITCH) || AxisUtil.axisPing.get(AxisUtil.Axis.YAW) ) {
            return mouseSensitivity.getValue() * 0.17;
        }

        return mouseSensitivity.getValue();
    }
}
