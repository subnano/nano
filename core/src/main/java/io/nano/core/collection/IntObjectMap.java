package io.nano.core.collection;

public interface IntObjectMap<V> {

    /**
     * Indicates whether or not the map contains a value for the given key.
     */
    boolean containsKey(final int key);

    /**
     * Retrieves the entry referenced by the given key from the map.
     *
     * @param key the key of the map entry.
     * @return the value for this key or null if there is no mapping.
     */
    V get(final int key);

    /**
     * Puts the given value into the map.
     *
     * @param key the key of the map entry.
     * @param value the value of the entry.
     * @return the previous value for this key or null if there was no previous mapping.
     */
    V put(final int key, final V value);

    /**
     * Removes the entry referenced by the given key from the map.
     *
     * @param key the key of the map entry.
     * @return the previous value for this key or null if there was no previous mapping.
     */
    V remove(final int key);

    /**
     * Returns the current size of the map.
     *
     * @return the size of the map.
     */
    int size();

    /**
     * Custom iterator that avoids object allocation
     */
    IntIterator iterator();

}
