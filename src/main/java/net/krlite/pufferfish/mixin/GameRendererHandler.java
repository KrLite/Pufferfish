package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.render.extra.ExtraInGameHudRenderer;
import net.krlite.pufferfish.render.extra.ExtraRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererHandler {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V")
            )
    )
    private void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        // Ignore Game Hud Visibilities
        MinecraftClient client = MinecraftClient.getInstance();
        MatrixStack extra = new MatrixStack();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        ExtraRenderer.render(extra);

        RenderSystem.disableBlend();
        RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

        // In-Game Hud
        if ( client.currentScreen == null ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ExtraInGameHudRenderer.render(extra);

            RenderSystem.disableBlend();
            RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
        }
    }
}
