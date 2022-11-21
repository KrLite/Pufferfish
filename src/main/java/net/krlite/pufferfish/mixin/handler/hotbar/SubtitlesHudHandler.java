package net.krlite.pufferfish.mixin.handler.hotbar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.config.PuffConfigs;
import net.krlite.pufferfish.render.renderer.PuffProxiedRenderer;
import net.krlite.pufferfish.render.PuffRenderer;
import net.krlite.pufferfish.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(SubtitlesHud.class)
public class SubtitlesHudHandler {
    @Shadow @Final private List<SubtitlesHud.SubtitleEntry> entries;

    @Shadow @Final private MinecraftClient client;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"
            )
    )
    private void renderBackground(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        if ( !PuffConfigs.enableChatAnimation ) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            PuffRenderer.COLORED.fillColored(
                    matrixStack,
                    xBegin, yBegin,
                    xEnd,   yEnd,
                    ColorUtil.castAlpha(PuffConfigs.chatBackgroundColor, MinecraftClient.getInstance().options.getTextBackgroundOpacity().getValue().floatValue())
            );

            RenderSystem.disableBlend();
        }
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void render(MatrixStack matrixStack, CallbackInfo ci) {
        Iterator<SubtitlesHud.SubtitleEntry> iterator = entries.iterator();
        float width = 0, height = entries.size() * (client.textRenderer.fontHeight + 1) + 2;

        while ( iterator.hasNext() ) {
            SubtitlesHud.SubtitleEntry subtitleEntry = iterator.next();
            if ( subtitleEntry.getTime() + 3000L <= Util.getMeasuringTimeMs() ) {
                iterator.remove();
                continue;
            }
            width = Math.max(width, client.textRenderer.getWidth(subtitleEntry.getText()));
        }
        width += 4 + width == 0 ? 0
                : this.client.textRenderer.getWidth("<")
                + this.client.textRenderer.getWidth(" ") * 2
                + this.client.textRenderer.getWidth(">");

        PuffProxiedRenderer.SUBTITLES_HUD.setSize(width, height);
    }
}
