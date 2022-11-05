package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.interaction_map.util.ClientAnchorProvider;
import net.krlite.pufferfish.util.ChatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.pufferfish.render.CrosshairPuffer.crosshairScaleTarget;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Final public GameOptions options;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "handleInputEvents", at = @At("TAIL"))
    private void handleInputEvents(CallbackInfo ci) {
        crosshairScaleTarget =
                options.attackKey.isPressed()
                        ? !options.useKey.isPressed()
                                // Attacking
                                ? PuffConfigs.crosshairSize * (1 + PuffConfigs.crosshairPuff)
                                // Both
                                : PuffConfigs.crosshairSize
                        : options.useKey.isPressed()
                                // Using
                                ? PuffConfigs.crosshairSize * MathHelper.clamp(1.0 - PuffConfigs.crosshairPuff * 0.6, 0.3, 1.0)
                                // None
                                : PuffConfigs.crosshairSize;
    }

    @Inject(method = "openChatScreen", at = @At("TAIL"))
    private void openChatScreen(String text, CallbackInfo ci) {
        ChatUtil.chatBackgroundOpacity = 0.0;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if ( player != null ) {
            if ( player.isDead() ) {
                ClientAnchorProvider.setDeathPos(player);
            }
        }
    }
}
