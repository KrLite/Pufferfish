package net.krlite.pufferfish.mixin;

import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.TitleUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if ( currentScreen instanceof ChatScreen ) {
            ChatUtil.chatBackgroundOpacityTarget = 1.0;
        } else {
            ChatUtil.chatBackgroundOpacityTarget = 0.0;
        }
    }

    @Inject(method = "setScreen", at = @At("TAIL"))
    private void setScreen(@Nullable Screen screen, CallbackInfo ci) {
        if ( !(screen instanceof TitleScreen) && PuffConfigs.enableTitleAnimation ) {
            TitleUtil.resetMeasuringStartTime();
        }
    }
}
