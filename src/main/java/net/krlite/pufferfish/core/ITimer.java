package net.krlite.pufferfish.core;

import net.krlite.pufferfish.math.ClassID;
import net.minecraft.util.Util;

import static net.krlite.pufferfish.core.CoreStorage.TIMER;

public interface ITimer {
    default long queue() {
        return Util.getMeasuringTimeMs();
    }

    default ClassID regTimer() {
        long time = queue();
        ClassID id = new ClassID(getClass(), time);

        if ( TIMER.containsKey(id.get()) ) {
            TIMER.replace(id.get(), time);
        } else {
            TIMER.put(id.get(), time);
        }
        return id;
    }

    default long getTimerOffset(ClassID id) {
        return TIMER.get(id.get());
    }

    default long getTimer(ClassID id) {
        return queue() - TIMER.get(id.get());
    }

    default void resetTimer(ClassID id) {
        TIMER.replace(id.get(), queue());
    }

    default void delTimer(ClassID id) {
        TIMER.remove(id.get());
    }
}
