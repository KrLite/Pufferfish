package net.krlite.pufferfish.interaction_map.util;

import net.krlite.pufferfish.interaction_map.util.solver.AnchorSolver;
import net.krlite.pufferfish.math.DimensionalVec3d;
import net.krlite.pufferfish.math.solver.AngleSolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class AnchorProvider {
    public static double flatAngle(PlayerEntity player, double angle) {
        return powAngle(player, angle, 1);
    }

    public static double powAngle(PlayerEntity player, double angle, double exp) {
        double
                cameraAngle = AngleSolver.clockwiseToPositive((player.getYaw() % 360 + 360 + 270) % 360),
                includedAngle = AngleSolver.positiveIncludePositive(cameraAngle, angle);
        return AnchorSolver.horizontalProjection(
                includedAngle * Math.pow(Math.abs(includedAngle) / 180, exp),
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0
        );
    }

    public static Optional<Double> dimensionalPosition(PlayerEntity player, DimensionalVec3d position) {
        return AnchorSolver.positionInSight(
                player, position,
                MinecraftClient.getInstance().getWindow().getScaledWidth(), true
        );
    }

    public static Optional<Double> dimensionalDistance(PlayerEntity player, DimensionalVec3d position) {
        return position.distance(new DimensionalVec3d(player));
    }

    public static Optional<Double> lastDeathPosition(PlayerEntity player) {
        if ( player.getLastDeathPos().isPresent() ) {
            return dimensionalPosition(player, new DimensionalVec3d(player.getLastDeathPos().get()));
        }

        return Optional.empty();
    }

    public static Optional<Double> lastDeathDistance(PlayerEntity player) {
        if ( player.getLastDeathPos().isPresent() ) {
            return dimensionalDistance(player, new DimensionalVec3d(player.getLastDeathPos().get()));
        }

        return Optional.empty();
    }
}
