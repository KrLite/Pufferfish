package net.krlite.pufferfish.core;

public interface IHashable {
    default int hash() {
        return getClass().getName().hashCode();
    }

    default int hash(String value) {
        return (getClass().getName() + ":" + value).hashCode();
    }

    default <T> int hash(Class<T> c) {
        return c.getName().hashCode();
    }

    default <T> int hash(Class<T> c, String value) {
        return (c.getName() + ":" + value).hashCode();
    }
}
