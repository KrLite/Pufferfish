package net.krlite.pufferfish.render.extra;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ExtraInGameHudRenderer {
    public static void render(MatrixStack matrixStack, float tickDelta, long startTime, boolean tick) {
        MinecraftClient client = MinecraftClient.getInstance();
        int
                width = client.getWindow().getScaledWidth(),
                height = client.getWindow().getScaledHeight();
    }
}
