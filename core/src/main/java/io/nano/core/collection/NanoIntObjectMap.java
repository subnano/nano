package io.nano.core.collection;

import io.nano.core.util.Bits;
import io.nano.core.util.Maths;

import java.util.Arrays;

/**
 * Int-Object map based on ObjObjMap from the following repo:
 * https://github.com/mikvor/hashmapTest
 */
public class NanoIntObjectMap<V> implements IntObjectMap<V> {

    private static final int FREE_KEY = 0;
    private static final Object REMOVED_KEY = new Object();

    /**
     * Keys and values stored in same array for performance reasons
     */
    private Object[] data;

    /**
     * Value for the free key (if inserted into a map)
     */
    private V freeValue;

    /**
     * Indicates if value has been set using the free key
     */
    private boolean isFreeKeySet;

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
    private int mask2;

    public NanoIntObjectMap(final int size, final float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1) {
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive!");
        }
        if (!Maths.isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Size must be a power of two!");
        }
        final int capacity = NanoArrays.arraySize(size, fillFactor);
        this.mask = capacity - 1;
        this.mask2 = capacity * 2 - 1;
        this.fillFactor = fillFactor;

        data = new Object[capacity * 2];
        Arrays.fill(data, FREE_KEY);

        threshold = (int)(capacity * fillFactor);
    }

    @Override
    public boolean containsKey(int key) {
        // TODO implement containsKey
        return false;
    }

    @Override
    public V get(final int key) {
        int index = indexOf(key);

        if (key == FREE_KEY) {
            return isFreeKeySet ? freeValue : null;
        }

        int k = (int)data[index];

        // key matches return value
        if (k == key) {
            return (V)data[index + 1];
        }
        // key conflict, scan array
        while (true) {
            index = (index + 2) & mask2;
            k = (int) data[index];
            if (k == FREE_KEY) {
                return null;
            }
            if (k == key) {
                return (V)data[index + 1];
            }
        }
    }

    @Override
    public V put(final int key, final V value) {
        if (key == FREE_KEY) {
            return insertFreeKey(value);
        }

        int index = indexOf(key);
        int k;
        while (true) {
            k = data[index] == null ? FREE_KEY : (int) data[index];
            // check for empty slot
            if (k == FREE_KEY) {
                data[index] = key;
                data[index + 1] = value;
                if (size >= threshold) {
                    resize(data.length * 2); //size is set inside
                } else {
                    ++size;
                }
                return null;
            }

            // found a matching key so replace
            else if (k == key) {
                final Object ret = data[index + 1];
                data[index + 1] = value;
                return (V)ret;
            }
            // skip index to next key/value pair
            index = (index + 2) & mask2;
        }
    }

    @Override
    public V remove(final int key) {
        if (key == FREE_KEY)
            return removeFreeKey();

        int index = indexOf(key);
        int k;

        while (true) {
            k = (int) data[index];
            if (k == FREE_KEY)
                return null;
            else if (k == key) {
                final V previousValue = (V) data[index + 1];
                --size;
                // no need to set value - just shift other value down
                shiftKeys(index);
                return previousValue;
            }
            // skip index to next key/value pair
            index = (index + 2) & mask2;
        }
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the array index for the given key
     */
    private int indexOf(int key) {
        return (Bits.shuffle(key) & mask) << 1;
    }

    private V insertFreeKey(final V value) {
        final V previous = freeValue;
        if (!isFreeKeySet)
            ++size;
        isFreeKeySet = true;
        freeValue = value;
        return previous;
    }

    private V removeFreeKey() {
        final V previous = freeValue;
        if (isFreeKeySet)
            --size;
        freeValue = null;
        isFreeKeySet = false;
        return previous;
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
                if ((k = data[index] == null ? FREE_KEY : (int) data[index]) == FREE_KEY) {
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
        final Object[] oldData = data;

        data = new Object[newCapacity];
        size = isFreeKeySet ? 1 : 0;
        int oldKey;
        for (int i = 0; i < oldCapacity; i += 2) {
            oldKey = oldData[i] == null ? FREE_KEY : (int) oldData[i];
            if (oldKey != FREE_KEY)
                put(oldKey, (V) oldData[i + 1]);
        }
    }

}
