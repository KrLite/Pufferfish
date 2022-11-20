package net.krlite.pufferfish.math;

import net.krlite.pufferfish.PuffMod;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ClassID {
    private final int hashed;

    public <T> ClassID(Class<T> c) {
        hashed = c.getName().hashCode();
    }

    public <T> ClassID(Class<T> c, double key) {
        hashed = (c.getName() + key).hashCode();
    }

    public <T> ClassID(Class<T> c, String key) {
        hashed = (c.getName() + key).hashCode();
    }

    public int get() {
        return hashed;
    }

    public boolean parse(@NotNull ClassID id) {
        return hashed == id.get();
    }

    public String toString() {
        return getClass().getName() + " " + Integer.toHexString(hashed);
    }
}
