package net.krlite.pufferfish.mixin.trigger;

import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.core.IBooleanRanger;
import net.krlite.pufferfish.math.ClassID;
import net.krlite.pufferfish.util.ChatUtil;
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
public class MinecraftClientTrigger implements IBooleanRanger {
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
    private void setScreen(Screen screen, CallbackInfo ci) {
        if ( !(screen instanceof TitleScreen) && PuffConfigs.enableTitleAnimation ) {
            putBoolean(new ClassID(TitleScreen.class, "ShowTitle"), false);
        }
    }
}
