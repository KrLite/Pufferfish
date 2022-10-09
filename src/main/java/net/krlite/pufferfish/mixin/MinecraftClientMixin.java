package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
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
                        //None
                        : PuffConfigs.crosshairOriginal;

        if ( LOCK_XZ.wasPressed() && avaliableXZ ) {
            lingerY = 0;
            if ( lingerXZ == 0 ) {
                lingerXZ = PuffConfigs.lingerKeyTicks;
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("XZ"));
            } else {
                lingerXZ = 0;
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("XZ-L"));
            }
        }

        if ( LOCK_Y.wasPressed() && avaliableY ) {
            lingerXZ = 0;
            if ( lingerY == 0 ) {
                lingerY = PuffConfigs.lingerKeyTicks;
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("Y"));
            } else {
                lingerY = 0;
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("Y-L"));
            }
        }

        avaliableXZ = !LOCK_XZ.isPressed();
        avaliableY = !LOCK_Y.isPressed();
    }
}
