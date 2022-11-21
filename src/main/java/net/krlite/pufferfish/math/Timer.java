package net.krlite.pufferfish.math;

import net.minecraft.util.Util;

public class Timer {
    private final long init;

    public Timer() {
        init = Util.getMeasuringTimeMs();
    }

    public Timer(long init) {
        this.init = init;
    }

    public long initializedTime() {
        return init;
    }

    public long queue() {
        return Util.getMeasuringTimeMs() - init;
    }

    public double queue(int tenfolds) {
        return (Util.getMeasuringTimeMs() - init) / Math.pow(10, tenfolds);
    }

    public long calculate(long time) {
        return time - init;
    }
}
