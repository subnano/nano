package io.nano.core.collection;

public interface IntObjectMap<V> {

    boolean containsKey(final int key);

    V get(final int key);

    V put(final int key, final V value);

    V remove(final int key);

    int size();

}
