package net.krlite.pufferfish.interaction_map.util.solver;

import net.krlite.pufferfish.math.DimensionalVec3d;
import net.krlite.pufferfish.math.solver.CoordinateSolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class AnchorSolver {
    public static Optional<Double> positionInSight(PlayerEntity player, DimensionalVec3d dst, float wideness, boolean dimensional) {
        return CoordinateSolver.angleInFrontOfCamera(dst, player, !dimensional)
                .map(
                        angle -> horizontalProjection(
                                angle,
                                wideness / 2.0
                        )
                );
    }

    public static double horizontalProjection(double angle) {
        return horizontalProjection(angle, true);
    }

    public static double horizontalProjection(double angle, boolean fovAffected) {
        return horizontalProjection(angle, 1.0, fovAffected);
    }

    public static double horizontalProjection(double angle, double multiplier) {
        return horizontalProjection(angle, multiplier, true);
    }

    public static double horizontalProjection(double angle, double multiplier, boolean fovAffected) {
        GameOptions options = MinecraftClient.getInstance().options;

        return multiplier * (1 + angle / 50.0 * (fovAffected ? Math.pow(70.0 / options.getFov().getValue(), 1.25) : 1.0));
    }
}
