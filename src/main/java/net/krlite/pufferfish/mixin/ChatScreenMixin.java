package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.pufferfish.PuffMod;
import net.krlite.pufferfish.util.ChatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends DrawableHelper {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    private void render(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        MinecraftClient client = MinecraftClient.getInstance();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) ChatUtil.backgroundOpacity);
        fill(
                matrixStack, -2, y1,
                client.currentScreen.width + 2, client.currentScreen.height + 2,
                client.options.getTextBackgroundColor(Integer.MIN_VALUE)
        );
    }
}
