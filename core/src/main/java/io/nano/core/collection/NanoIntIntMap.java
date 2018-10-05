package io.nano.core.collection;

import io.nano.core.util.Bits;
import io.nano.core.util.Maths;

/**
 * Int-Int map based on IntIntMap4a from the following repo:
 * https://github.com/mikvor/hashmapTest
 */
public class NanoIntIntMap implements IntIntMap {

    private static final int FREE_KEY = 0;

    private static final int KEY_MISSING = -1;

    /**
     * Keys and values
     */
    private int[] data;

    /**
     * Do we have 'free' key in the map?
     * We need to use zero to indicate an empty slot while still support using a key equal to zero
     */
    private boolean isFreeKeySet;

    /**
     * Value of 'free' key
     */
    private int freeValue = KEY_MISSING;

    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float fillFactor;

    /**
     * Resize map once it reaches this size
     */
    private int threshold;

    /**
     * Current map size
     */
    private int size;

    /**
     * Mask to calculate the original position
     */
    private int mask;

    /**
     * Mask to wrap the actual array pointer
     */
    private int mask2;

    /**
     * mutable iterator used to iterate through entire collection
     */
    private final NanoIntIterator iterator;

    public NanoIntIntMap(final int size, final float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1) {
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive!");
        }
        // Using power-of-two array capacity avoids expensive % operations
        if (!Maths.isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Size must be a power of two!");
        }
        final int capacity = NanoArrays.arraySize(size, fillFactor);
        this.mask = capacity - 1;
        this.mask2 = capacity * 2 - 1;
        this.fillFactor = fillFactor;
        this.data = new int[capacity * 2];
        this.threshold = (int)(capacity * fillFactor);
        this.iterator = new NanoIntIterator();
    }

    @Override
    public boolean containsKey(int key) {
        // TODO implement containsKey
        return false;
    }

    @Override
    public int get(final int key) {
        if (key == FREE_KEY) {
            return isFreeKeySet ? freeValue : KEY_MISSING;
        }

        int index = indexOf(key);
        int k;

        // key conflict, scan array
        while (true) {
            k = data[index];
            if (k == FREE_KEY) {
                return KEY_MISSING;
            }
            if (k == key) {
                return data[index + 1];
            }
            // skip index to next key/value pair
            index = (index + 2) & mask2;
        }
    }

    @Override
    public int put(final int key, final int value) {
        if (key == FREE_KEY) {
            final int previousValue = freeValue;
            if (!isFreeKeySet) {
                ++size;
            }
            isFreeKeySet = true;
            freeValue = value;
            return previousValue;
        }

        int index = indexOf(key);
        int k;
        while (true) {
            k = data[index];
            // check for empty slot
            if (k == FREE_KEY) {
                data[index] = key;
                data[index + 1] = value;
                if (size >= threshold) {
                    resize(data.length * 2);
                } else {
                    ++size;
                }
                return KEY_MISSING;
            }
            // found a matching key so replace
            else if (k == key) {
                final int previousValue = data[index + 1];
                data[index + 1] = value;
                return previousValue;
            }
            // skip index to next key/value pair
            index = (index + 2) & mask2;
        }
    }

    @Override
    public int remove(final int key) {
        if (key == FREE_KEY) {
            if (!isFreeKeySet) {
                return KEY_MISSING;
            }
            isFreeKeySet = false;
            --size;
            return freeValue; //value is not cleaned
        }

        int index = indexOf(key);
        int k;
        while (true) {
            k = data[index];
            if (k == key) {
                final int previousValue = data[index + 1];
                // no need to set value - just shift other value down
                shiftKeys(index);
                --size;
                return previousValue;
            } else if (k == FREE_KEY) {
                return KEY_MISSING;
            }
            // skip index to next key/value pair
            index = (index + 2) & mask2;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public IntIterator iterator() {
        iterator.reset();
        return iterator;
    }

    /**
     * Returns the array index for the given key
     */
    private int indexOf(int key) {
        return (Bits.shuffle(key) & mask) << 1;
    }

    /**
     * Entries with same key are shifted when we remove items from the array.
     *
     * Removals are more costly but array access using {@link #get(int)} remain optimal.
     */
    private int shiftKeys(int index) {
        // Shift entries with the same hash.
        int last, slot;
        int k;
        while (true) {
            index = ((last = index) + 2) & mask2;
            while (true) {
                if ((k = data[index]) == FREE_KEY) {
                    data[last] = FREE_KEY;
                    return last;
                }
                slot = indexOf(k);
                if (last <= index ? last >= slot || slot > index : last >= slot && slot > index) {
                    break;
                }
                // skip index to next key/value pair
                index = (index + 2) & mask2;
            }
            data[last] = k;
            data[last + 1] = data[index + 1];
        }
    }

    /**
     * Resize the array when the capacity is reached.
     * This obviously a costly operation so ensuring that the collection is sized correctly
     * when created is essential.
     * @param newCapacity twice the original capacity since we are storing the key and value in the same array
     */
    private void resize(final int newCapacity) {
        threshold = (int)(newCapacity / 2 * fillFactor);
        mask = newCapacity / 2 - 1;
        mask2 = newCapacity - 1;

        final int oldCapacity = data.length;
        final int[] oldData = data;

        data = new int[newCapacity];
        size = isFreeKeySet ? 1 : 0;

        for (int i = 0; i < oldCapacity; i += 2) {
            final int oldKey = oldData[i];
            if (oldKey != FREE_KEY) {
                put(oldKey, oldData[i + 1]);
            }
        }
    }

    class NanoIntIterator implements IntIterator {

        // an index of -1 indicates state prior to start
        private int index = -2;

        @Override
        public int nextKey() {
            while ((index += 2) < mask2) {
                int key = data[index];
                if (key == FREE_KEY && isFreeKeySet) {
                    return key;
                }
                if (key != FREE_KEY) {
                    return key;
                }
            }
            return KEY_MISSING;
        }

        /**
         * Not visible to API through interface but called by the collection
         */
        void reset() {
            index = -2;
        }
    }
}
