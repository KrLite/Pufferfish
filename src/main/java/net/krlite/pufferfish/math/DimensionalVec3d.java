package net.krlite.pufferfish.math;

import net.krlite.pufferfish.math.solver.CoordinateSolver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class DimensionalVec3d {
    private final RegistryKey<World> dimension;
    private final Vec3d vec3d;

    private DimensionalVec3d(RegistryKey<World> dimension, Vec3d vec3d) {
        this.dimension = dimension;
        this.vec3d = vec3d;
    }

    public static DimensionalVec3d create(Entity entity) {
        return new DimensionalVec3d(entity.getWorld().getRegistryKey(), entity.getPos());
    }

    public static DimensionalVec3d create(GlobalPos globalPos) {
        return new DimensionalVec3d(globalPos.getDimension(), Vec3d.of(globalPos.getPos()));
    }

    public static DimensionalVec3d create(RegistryKey<World> dimension, Vec3d vec3d) {
        return new DimensionalVec3d(dimension, vec3d);
    }

    public RegistryKey<World> getDimension() {
        return dimension;
    }

    public Vec3d getVec3d() {
        return vec3d;
    }

    public Optional<Double> distance(DimensionalVec3d dst) {
        return CoordinateSolver.distance(this, dst);
    }

    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }

        if ( o == null || this.getClass() != o.getClass() ) {
            return false;
        }

        DimensionalVec3d d = (DimensionalVec3d) o;
        return Objects.equals(this.dimension, d.dimension) && Objects.equals(this.vec3d, d.vec3d);
    }

    public int hashCode() {
        return Objects.hash(this.dimension, this.vec3d);
    }

    public String toString() {
        return this.dimension + " " + this.vec3d;
    }
}
