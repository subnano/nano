package io.nano.core.collection;

public interface IntObjectMap<V> {

    V get(final int key);

    int put(final int key, final V value);

    int remove(final int key);

    int size();

}
