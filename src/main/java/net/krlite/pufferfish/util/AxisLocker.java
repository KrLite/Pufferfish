package net.krlite.pufferfish.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AxisLocker {
    private static final float delta = 0.25F;
    public static UUID playerUuid;

    public enum Axis {
        PITCH, YAW;
    }

    public static Map<Axis, Boolean> axisLock = new HashMap<>(), axisPing = new HashMap<>();

    public static Map<Axis, Float> axisStatic = new HashMap<>();

    public static void lockPitch(PlayerEntity player) {
        axisStatic.replace(Axis.PITCH, player.getPitch());
    }

    public static void lockYaw(PlayerEntity player) {
        axisStatic.replace(Axis.YAW, player.getYaw());
    }

    public static void applyPitch(float pitch) {
        axisStatic.replace(Axis.PITCH, pitch);
        axisPing.replace(Axis.PITCH, true);
    }

    public static void applyYaw(float yaw) {
        axisStatic.replace(Axis.YAW, yaw);
        axisPing.replace(Axis.YAW, true);
    }

    public static void applyPitch(PlayerEntity player) {
        float pitch = player.getPitch();
        applyPitch(
                (pitch < 45)
                        ? (pitch > -45)
                                ? 0.0F
                                : -90.0F
                        : 90.0F
        );
    }

    public static void applyYaw(PlayerEntity player) {
        float yaw = player.getYaw();
        applyYaw(
                (Math.abs(yaw) % 90) >= 45
                        ? yaw < 0
                                ? ((int) (yaw / 90) - 1) * 90.0F
                                : ((int) (yaw / 90) + 1) * 90.0F
                        : (int) (yaw / 90) * 90
        );
    }

    public static void registerAxisMaps() {
        axisLock.putAll(ImmutableMap.of(
                Axis.PITCH, false,
                Axis.YAW, false
        ));

        axisPing.putAll(ImmutableMap.of(
                Axis.PITCH, false,
                Axis.YAW, false
        ));

        axisStatic.putAll(ImmutableMap.of(
                Axis.PITCH, 0.0F,
                Axis.YAW, 0.0F
        ));
    }

    public static void update(PlayerEntity player) {
        // Ping Axis
        if ( axisPing.get(Axis.PITCH) && Math.abs(player.getPitch() - axisStatic.get(Axis.PITCH)) >= 0.1F ) {
            player.setPitch(MathHelper.lerp(
                    delta,
                    player.getPitch(),
                    axisStatic.get(Axis.PITCH)
            ));
        } else {
            axisPing.replace(Axis.PITCH, false);
        }

        if ( axisPing.get(Axis.YAW) && Math.abs(player.getYaw() - axisStatic.get(Axis.YAW)) >= 0.1F ) {
            player.setYaw(MathHelper.lerp(
                    delta,
                    player.getYaw(),
                    axisStatic.get(Axis.YAW)
            ));
        } else {
            axisPing.replace(Axis.YAW, false);
        }
    }
}
