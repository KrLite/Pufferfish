package net.krlite.pufferfish.render.extra;

import net.krlite.pufferfish.interaction_map.render.AnchorRenderer;
import net.krlite.pufferfish.interaction_map.util.AnchorProvider;
import net.krlite.pufferfish.math.PreciseColor;
import net.krlite.pufferfish.render.CameraOverlayHandler;
import net.krlite.pufferfish.util.AxisUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.ScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Optional;

public class ExtraInGameHudRenderer {
    public static void render(MatrixStack matrixStack, float tickDelta, long startTime, boolean tick) {
        MinecraftClient client = MinecraftClient.getInstance();
        int
                width = client.getWindow().getScaledWidth(),
                height = client.getWindow().getScaledHeight();
    }
}
