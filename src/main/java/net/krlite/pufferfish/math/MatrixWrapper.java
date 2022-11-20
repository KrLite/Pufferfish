package net.krlite.pufferfish.math;

import net.krlite.pufferfish.math.solver.GridSolver;
import net.minecraft.util.math.MathHelper;
import oshi.util.tuples.Pair;

public class MatrixWrapper {
    private final float xLU, xLD, xRD, xRU;
    private final float yLU, yLD, yRD, yRU;

    private float lerp(float value, float target, float delta) {
        return value + (target - value) * delta;
    }

    private float half(float value, float target) {
        return (value + target) / 2.0F;
    }

    public MatrixWrapper(
            float xLU, float yLU,
            float xLD, float yLD,
            float xRD, float yRD,
            float xRU, float yRU
    ) {
        this.xLU = xLU;
        this.yLU = yLU;
        this.xLD = xLD;
        this.yLD = yLD;
        this.xRD = xRD;
        this.yRD = yRD;
        this.xRU = xRU;
        this.yRU = yRU;
    }

    public MatrixWrapper(
            float xBegin,   float yBegin,
            float xEnd,     float yEnd
    ) {
        Pair<Pair<Float, Float>, Pair<Float, Float>> grid = GridSolver.gridXY(xBegin, yBegin, xEnd, yEnd);

        xBegin  = grid.getA().getA();
        xEnd    = grid.getA().getB();
        yBegin  = grid.getB().getA();
        yEnd    = grid.getB().getB();

        xLU = xBegin;
        yLU = yBegin;
        xLD = xBegin;
        yLD = yEnd;
        xRD = xEnd;
        yRD = yEnd;
        xRU = xEnd;
        yRU = yBegin;
    }

    public float xLU() {
        return this.xLU;
    }

    public float xLD() {
        return this.xLD;
    }

    public float xRD() {
        return this.xRD;
    }

    public float xRU() {
        return this.xRU;
    }

    public float yLU() {
        return this.yLU;
    }

    public float yLD() {
        return this.yLD;
    }

    public float yRD() {
        return this.yRD;
    }

    public float yRU() {
        return this.yRU;
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean matrix) {
        if ( matrix ){
            return getClass().getName() + " ->\n"
                    + "[" + this.xLU + "," + this.yLU + "],"
                    + "[" + this.xRU + "," + this.yRU + "]\n"
                    + "[" + this.xLD + "," + this.yLD + "],"
                    + "[" + this.xRD + "," + this.yRD + "]";
        } else {
            return getClass().getName()
                    + "{LU=[" + this.xLU + "," + this.yLU + "],"
                    + "LD=[" + this.xLD + "," + this.yLD + "],"
                    + "RD=[" + this.xRD + "," + this.yRD + "],"
                    + "RU=[" + this.xRU + "," + this.yRU + "]}";
        }
    }

    public MatrixWrapper minimal(MatrixWrapper wrapper) {
        return new MatrixWrapper(
                Math.min(this.xLU, wrapper.xLU),
                Math.min(this.yLU, wrapper.yLU),
                Math.min(this.xLD, wrapper.xLD),
                Math.min(this.yLD, wrapper.yLD),
                Math.min(this.xRD, wrapper.xRD),
                Math.min(this.yRD, wrapper.yRD),
                Math.min(this.xRU, wrapper.xRU),
                Math.min(this.yRU, wrapper.yRU)
        );
    }

    public MatrixWrapper maximal(MatrixWrapper wrapper) {
        return new MatrixWrapper(
                Math.max(this.xLU, wrapper.xLU),
                Math.max(this.yLU, wrapper.yLU),
                Math.max(this.xLD, wrapper.xLD),
                Math.max(this.yLD, wrapper.yLD),
                Math.max(this.xRD, wrapper.xRD),
                Math.max(this.yRD, wrapper.yRD),
                Math.max(this.xRU, wrapper.xRU),
                Math.max(this.yRU, wrapper.yRU)
        );
    }

    public MatrixWrapper blend(MatrixWrapper wrapper) {
        return blend(wrapper, 0.5F);
    }

    public MatrixWrapper blend(MatrixWrapper wrapper, float delta) {
        return new MatrixWrapper(
                lerp(this.xLU, wrapper.xLU, delta),
                lerp(this.yLU, wrapper.yLU, delta),
                lerp(this.xLD, wrapper.xLD, delta),
                lerp(this.yLD, wrapper.yLD, delta),
                lerp(this.xRD, wrapper.xRD, delta),
                lerp(this.yRD, wrapper.yRD, delta),
                lerp(this.xRU, wrapper.xRU, delta),
                lerp(this.yRU, wrapper.yRU, delta)
        );
    }

    public MatrixWrapper stretchLU(float x, float y) {
        return stretchLU(x, y, 1);
    }

    public MatrixWrapper stretchLD(float x, float y) {
        return stretchLD(x, y, 1);
    }

    public MatrixWrapper stretchRD(float x, float y) {
        return stretchRD(x, y, 1);
    }

    public MatrixWrapper stretchRU(float x, float y) {
        return stretchRU(x, y, 1);
    }

    public MatrixWrapper stretchLU(float x, float y, float delta) {
        return blend(
                new MatrixWrapper(
                        x, y,
                        half(this.xLD, x), half(this.yLD, y),
                        this.xRD, this.yRD,
                        half(this.xRU, x), half(this.yRU, y)
                ), delta
        );
    }

    public MatrixWrapper stretchLD(float x, float y, float delta) {
        return blend(
                new MatrixWrapper(
                        half(this.xLU, x), half(this.yLU, y),
                        x, y,
                        half(this.xRD, x), half(this.yRD, y),
                        this.xRU, this.yRU
                ), delta
        );
    }

    public MatrixWrapper stretchRD(float x, float y, float delta) {
        return blend(
                new MatrixWrapper(
                        this.xLU, this.yLU,
                        half(this.xLD, x), half(this.yLD, y),
                        x, y,
                        half(this.xRU, x), half(this.yRU, y)
                ), delta
        );
    }

    public MatrixWrapper stretchRU(float x, float y, float delta) {
        return blend(
                new MatrixWrapper(
                        half(this.xLU, x), half(this.yLU, y),
                        this.xLD, this.yLD,
                        half(this.xRD, x), half(this.yRD, y),
                        x, y
                ), delta
        );
    }

    public MatrixWrapper stretchTop(float y) {
        return stretchTop(y, 1);
    }

    public MatrixWrapper stretchBottom(float y) {
        return stretchBottom(y, 1);
    }

    public MatrixWrapper stretchLeft(float x) {
        return stretchLeft(x, 1);
    }

    public MatrixWrapper stretchRight(float x) {
        return stretchRight(x, 1);
    }

    public MatrixWrapper stretchTop(float y, float delta) {
        return blend(
                new MatrixWrapper(
                        xLU, y,
                        xLD, yLD,
                        xRD, yRD,
                        xRU, y
                ), delta
        );
    }

    public MatrixWrapper stretchBottom(float y, float delta) {
        return blend(
                new MatrixWrapper(
                        xLU, yLU,
                        xLD, y,
                        xRD, y,
                        xRU, yRU
                ), delta
        );
    }

    public MatrixWrapper stretchLeft(float x, float delta) {
        return blend(
                new MatrixWrapper(
                        x,   yLU,
                        x,   yLD,
                        xRD, yRD,
                        xRU, yRU
                ), delta
        );
    }

    public MatrixWrapper stretchRight(float x, float delta) {
        return blend(
                new MatrixWrapper(
                        xLU, yLU,
                        xLD, yLD,
                        x,   yRD,
                        x,   yRU
                ), delta
        );
    }

    public MatrixWrapper squeezeTop(float delta) {
        return new MatrixWrapper(
                xLU, lerp(yLU, yLD, delta),
                xLD, yLD,
                xRD, yRD,
                xRU, lerp(yRU, yRD, delta)
        );
    }

    public MatrixWrapper squeezeBottom(float delta) {
        return new MatrixWrapper(
                xLU, yLU,
                xLD, lerp(yLD, yLU, delta),
                xRD, lerp(yRD, yRU, delta),
                xRU, yRU
        );
    }

    public MatrixWrapper squeezeLeft(float delta) {
        return new MatrixWrapper(
                lerp(xLU, xRU, delta), yLU,
                lerp(xLD, xRD, delta), yLD,
                xRD, yRD,
                xRU, yRU
        );
    }

    public MatrixWrapper squeezeRight(float delta) {
        return new MatrixWrapper(
                xLU, yLU,
                xLD, yLD,
                lerp(xRD, xLD, delta), yRD,
                lerp(xRU, xLU, delta), yRU
        );
    }

    public MatrixWrapper flipHorizontal() {
        return flipHorizontal(1);
    }

    public MatrixWrapper flipVertical() {
        return flipVertical(1);
    }

    public MatrixWrapper flipDiagonalLDRU() {
        return flipDiagonalLDRU(1);
    }

    public MatrixWrapper flipDiagonalLURD() {
        return flipDiagonalLURD(1);
    }

    public MatrixWrapper flipHorizontal(float delta) {
        return blend(
                new MatrixWrapper(
                        xRU, yRU,
                        xRD, yRD,
                        xLD, yLD,
                        xLU, yLU
                ), delta
        );
    }

    public MatrixWrapper flipVertical(float delta) {
        return blend(
                new MatrixWrapper(
                        xLD, yLD,
                        xLU, yLU,
                        xRU, yRU,
                        xRD, yRD
                ), delta
        );
    }

    public MatrixWrapper flipDiagonalLDRU(float delta) {
        return blend(
                new MatrixWrapper(
                        xRD, yRD,
                        xLD, yLD,
                        xLU, yLU,
                        xRU, yRU
                ), delta
        );
    }

    public MatrixWrapper flipDiagonalLURD(float delta) {
        return blend(
                new MatrixWrapper(
                        xLU, yLU,
                        xRU, yRU,
                        xRD, yRD,
                        xLD, yLD
                ), delta
        );
    }
}
