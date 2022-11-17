package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.render.extra.ExtraBeforeRenderer;
import net.krlite.pufferfish.render.extra.ExtraInGameHudRenderer;
import net.krlite.pufferfish.render.extra.ExtraAfterRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererHandler {
    @Shadow public abstract void tick();

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void renderBefore(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Before
        ExtraBeforeRenderer.render(PuffRenderer.extraBefore, tickDelta, startTime, tick);

        RenderSystem.disableBlend();
        RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiler/Profiler;pop()V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V")
            )
    )
    private void renderAfter(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if ( client.currentScreen == null ) {
            // In-Game
            ExtraInGameHudRenderer.render(PuffRenderer.extraInGame, tickDelta, startTime, tick);
        }

        // After
        ExtraAfterRenderer.render(PuffRenderer.extraAfter, tickDelta, startTime, tick);

        RenderSystem.disableBlend();
        RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
    }
}
