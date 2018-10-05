package io.nano.core.collection;

public interface IntIterator {

    /**
     * Returns the next key in the iteration
     * @return the next key or -1 if no more keys found in the collection
     */
    int nextKey();
}
