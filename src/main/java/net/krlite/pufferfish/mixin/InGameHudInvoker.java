package net.krlite.pufferfish.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudInvoker {
    @Invoker("renderOverlay")
    void invokeRenderOverlay(Identifier texture, float opacity);
}