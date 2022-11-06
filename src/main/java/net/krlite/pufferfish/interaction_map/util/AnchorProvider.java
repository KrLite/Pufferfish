package net.krlite.pufferfish.interaction_map.util;

import net.krlite.pufferfish.interaction_map.util.solver.AnchorSolver;
import net.krlite.pufferfish.math.DimensionalVec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class AnchorProvider {
    public static Optional<Double> lastDeathPosition(PlayerEntity player, float scale) {
        if ( player.getLastDeathPos().isPresent() ) {
            return AnchorSolver.positionInSight(
                    player, DimensionalVec3d.create(player.getLastDeathPos().get()),
                    MinecraftClient.getInstance().getWindow().getScaledWidth() * scale, true
            );
        }

        return Optional.empty();
    }

    public static Optional<Double> lastDeathDistance(PlayerEntity player) {
        if ( player.getLastDeathPos().isPresent() ) {
            return DimensionalVec3d.create(player).distance(DimensionalVec3d.create(player.getLastDeathPos().get()));
        }

        return Optional.empty();
    }
}
