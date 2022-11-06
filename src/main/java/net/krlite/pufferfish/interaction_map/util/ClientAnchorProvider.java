package net.krlite.pufferfish.interaction_map.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientAnchorProvider {
    public static World deathWorld;
    public static BlockPos deathPos;

    public static void setDeathPos(ClientPlayerEntity player) {
        deathWorld = player.world;
        deathPos = player.getBlockPos();
    }

    public static Object resolveLastDeathPos(ClientPlayerEntity player, float scale) {
        World world = player.world;

        if (
                deathPos != null
                        && world.getRegistryKey() == deathWorld.getRegistryKey()
                        && !player.isDead()
        ) {
            double
                    dstAngle = Math.atan2(
                            deathPos.getZ() + 0.5 - player.getZ(),
                            deathPos.getX() + 0.5 - player.getX()
                    ) * 180 / Math.PI + 180,
                    cameraAngle = (player.getYaw() % 360 + 360 + 270) % 360;

            return (float) (horizontalProjection(
                    anchorAngle(cameraAngle, dstAngle) * scale, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0
            ));
        }

        return null;
    }

    public static Object resolveDeathDistance(ClientPlayerEntity player) {
        World world = player.world;

        if (
                deathPos != null
                        && world.getRegistryKey() == deathWorld.getRegistryKey()
                        && !player.isDead()
        ) {
            return (float) deathPos.getSquaredDistance(player.getPos());
        }

        return null;
    }

    private static double horizontalProjection(double angle) {
        return horizontalProjection(angle, true);
    }

    private static double horizontalProjection(double angle, boolean fovAffected) {
        return horizontalProjection(angle, 1.0, fovAffected);
    }

    private static double horizontalProjection(double angle, double multiplier) {
        return horizontalProjection(angle, multiplier, true);
    }

    private static double horizontalProjection(double angle, double multiplier, boolean fovAffected) {
        GameOptions options = MinecraftClient.getInstance().options;

        return multiplier * (1 - angle / 50.0 * (fovAffected ? 2 - options.fov / 70.0 : 1.0));
    }

    private static double anchorAngle(double srcAngle, double dstAngle) {
        return srcAngle - dstAngle + (srcAngle < dstAngle - 180 ? 360 : 0);
    }
}
