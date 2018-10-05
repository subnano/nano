package io.nano.core.collection;

/**
 * A high performance, zero allocation primitive map
 *
 * Does not implement {@link java.util.Map#keySet()} and {@link java.util.Map#values()} which would result in object allocation.
 *
 * Uses a mutable iterator instead.
 *
 * TODO putIfAbsent
 */
public interface IntIntMap {

    /**
     * Indicates whether or not the map contains a value for the given key.
     */
    boolean containsKey(final int key);

    /**
     * Retrieves the entry referenced by the given key from the map.
     *
     * @param key the key of the map entry.
     * @return the value for this key or {@code -1} if there is no mapping.
     */
    int get(final int key);

    /**
     * Puts the given value into the map.
     *
     * @param key the key of the map entry.
     * @param value the value of the entry.
     * @return the previous value for this key or {@code -1} if there was no previous mapping.
     */
    int put(final int key, final int value);

    /**
     * Removes the entry referenced by the given key from the map.
     *
     * @param key the key of the map entry.
     * @return the previous value for this key or {@code -1} if there was no previous mapping.
     */
    int remove(final int key);

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
