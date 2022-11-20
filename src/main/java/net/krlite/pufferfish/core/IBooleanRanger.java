package net.krlite.pufferfish.core;

import net.krlite.pufferfish.math.ClassID;

import static net.krlite.pufferfish.core.CoreStorage.BOOLEAN;

public interface IBooleanRanger {
    default void putBoolean(ClassID id, boolean value) {
        if ( BOOLEAN.containsKey(id.get()) ) {
            BOOLEAN.replace(id.get(), value);
        } else {
            BOOLEAN.put(id.get(), value);
        }
    }

    default boolean getBoolean(ClassID id) {
        return BOOLEAN.get(id.get());
    }

    default boolean getBooleanOrDefault(ClassID id, boolean value) {
        return BOOLEAN.getOrDefault(id.get(), value);
    }
}
