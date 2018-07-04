package io.nano.core.collection;

public interface IntIntMap {

    int get(final int key);

    int put(final int key, final int value);

    int remove(final int key);

    int size();

}
