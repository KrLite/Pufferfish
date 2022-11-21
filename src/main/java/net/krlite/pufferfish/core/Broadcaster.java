package net.krlite.pufferfish.core;

import java.util.HashMap;

public class Broadcaster {
    static final HashMap<Integer, Object> BROADCAST = new HashMap<>();

    public interface IBroadcaster {
        default void broadcast(int namespace, Object message) {
            BROADCAST.put(namespace, message);
        }

        default Object replaceBroadcast(int namespace, Object message) {
            if ( !BROADCAST.containsKey(namespace) ) {
                return new Object();
            }

            return BROADCAST.replace(namespace, message);
        }

        default Object cleanBroadcast(int namespace) {
            if ( !BROADCAST.containsKey(namespace) ) {
                return new Object();
            }

            return BROADCAST.remove(namespace);
        }

        default Object getBroadcast(int namespace) {
            if ( !BROADCAST.containsKey(namespace) ) {
                return new Object();
            }

            return BROADCAST.get(namespace);
        }

        default Object getBroadcastOrDefault(int namespace, Object defaultValue) {
            return BROADCAST.getOrDefault(namespace, defaultValue);
        }

        default int getBroadcasterSize() {
            return BROADCAST.size();
        }
    }
}
