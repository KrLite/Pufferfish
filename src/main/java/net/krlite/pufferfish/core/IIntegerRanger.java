package net.krlite.pufferfish.core;

import net.krlite.pufferfish.math.ClassID;

import static net.krlite.pufferfish.core.CoreStorage.INTEGER;

public interface IIntegerRanger {
    default void putInteger(ClassID id, int value) {
        if ( INTEGER.containsKey(id.get()) ) {
            INTEGER.replace(id.get(), value);
        } else {
            INTEGER.put(id.get(), value);
        }
    }

    default int getInteger(ClassID id) {
        return INTEGER.get(id.get());
    }

    default int getIntegerOrDefault(ClassID id, int value) {
        return INTEGER.getOrDefault(id.get(), value);
    }
}
