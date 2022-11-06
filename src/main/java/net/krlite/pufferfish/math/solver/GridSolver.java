package net.krlite.pufferfish.math.solver;

import oshi.util.tuples.Pair;

public class GridSolver {
    public static Pair<Pair<Float, Float>, Pair<Float, Float>> gridXY(
            float xBegin, float yBegin,
            float xEnd, float yEnd
    ) {
        return new Pair<>(
                xEnd < xBegin
                        ? new Pair<>(xEnd, xBegin)
                        : new Pair<>(xBegin, xEnd),
                yEnd < yBegin
                        ? new Pair<>(yEnd, yBegin)
                        : new Pair<>(yBegin, yEnd)
        );
    }

    public static Pair<Float, Float> linearAB(float a, float b) {
        return b < a
                ? new Pair<>(b, a)
                : new Pair<>(a, b);
    }
}
