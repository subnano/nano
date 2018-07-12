package io.nano.core.collection;

import java.util.Arrays;

/**
 * Int-Object map based on ObjObjMap from the following repo:
 * https://github.com/mikvor/hashmapTest
 */
public class NanoIntObjMap<V> {

    private static final int FREE_KEY = 0;
    private static final Object REMOVED_KEY = new Object();

    /**
     * Keys and values
     */
    private Object[] data;

    /**
     * Value for the null key (if inserted into a map)
     */
    private Object nullValue;
    private boolean hasNull;

    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float fillFactor;

    /**
     * We will resize a map once it reaches this size
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

    public NanoIntObjMap(final int size, final float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1)
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        if (size <= 0)
            throw new IllegalArgumentException("Size must be positive!");
        if ((size & (size - 1)) == 0)
            throw new IllegalArgumentException("Size must be a power of two!");
        final int capacity = NanoArrays.arraySize(size, fillFactor);
        this.mask = capacity - 1;
        this.mask2 = capacity * 2 - 1;
        this.fillFactor = fillFactor;

        data = new Object[capacity * 2];
        Arrays.fill(data, FREE_KEY);

        threshold = (int) (capacity * fillFactor);
    }

    public V get(final int key) {
        if (key == null)
            return (V) nullValue; //we null it on remove, so safe not to check a flag here

        int ptr = (key.hashCode() & mask) << 1;
        Object k = data[ptr];

        if (k == FREE_KEY)
            return null;  //end of chain already
        if (k.equals(key)) //we check FREE and REMOVED prior to this call
            return (V) data[ptr + 1];
        while (true) {
            ptr = (ptr + 2) & mask2; //that's next index
            k = data[ptr];
            if (k == FREE_KEY)
                return null;
            if (k.equals(key))
                return (V) data[ptr + 1];
        }
    }

    public V put(final int key, final V value) {
        if (key == null)
            return insertNullKey(value);

        int ptr = getStartIndex(key) << 1;
        Object k = data[ptr];

        if (k == FREE_KEY) //end of chain already
        {
            data[ptr] = key;
            data[ptr + 1] = value;
            if (size >= threshold)
                rehash(data.length * 2); //size is set inside
            else
                ++size;
            return null;
        } else if (k.equals(key)) //we check FREE and REMOVED prior to this call
        {
            final Object ret = data[ptr + 1];
            data[ptr + 1] = value;
            return (V) ret;
        }

        int firstRemoved = -1;
        if (k == REMOVED_KEY)
            firstRemoved = ptr; //we may find a key later

        while (true) {
            ptr = (ptr + 2) & mask2; //that's next index calculation
            k = data[ptr];
            if (k == FREE_KEY) {
                if (firstRemoved != -1)
                    ptr = firstRemoved;
                data[ptr] = key;
                data[ptr + 1] = value;
                if (size >= threshold)
                    rehash(data.length * 2); //size is set inside
                else
                    ++size;
                return null;
            } else if (k.equals(key)) {
                final Object ret = data[ptr + 1];
                data[ptr + 1] = value;
                return (V) ret;
            } else if (k == REMOVED_KEY) {
                if (firstRemoved == -1)
                    firstRemoved = ptr;
            }
        }
    }

    public V remove(final int key) {
        if (key == null)
            return removeNullKey();

        int ptr = getStartIndex(key) << 1;
        Object k = data[ptr];
        if (k == FREE_KEY)
            return null;  //end of chain already
        else if (k.equals(key)) //we check FREE and REMOVED prior to this call
        {
            --size;
            if (data[(ptr + 2) & mask2] == FREE_KEY)
                data[ptr] = FREE_KEY;
            else
                data[ptr] = REMOVED_KEY;
            final V ret = (V) data[ptr + 1];
            data[ptr + 1] = null;
            return ret;
        }
        while (true) {
            ptr = (ptr + 2) & mask2; //that's next index calculation
            k = data[ptr];
            if (k == FREE_KEY)
                return null;
            else if (k.equals(key)) {
                --size;
                if (data[(ptr + 2) & mask2] == FREE_KEY)
                    data[ptr] = FREE_KEY;
                else
                    data[ptr] = REMOVED_KEY;
                final V ret = (V) data[ptr + 1];
                data[ptr + 1] = null;
                return ret;
            }
        }
    }

    private V insertNullKey(final V value) {
        if (hasNull) {
            final Object ret = nullValue;
            nullValue = value;
            return (V) ret;
        } else {
            nullValue = value;
            ++size;
            return null;
        }
    }

    private V removeNullKey() {
        if (hasNull) {
            final Object ret = nullValue;
            nullValue = null;
            hasNull = false;
            --size;
            return (V) ret;
        } else {
            return null;
        }
    }

    public int size() {
        return size;
    }

    private void rehash(final int newCapacity) {
        threshold = (int) (newCapacity / 2 * fillFactor);
        mask = newCapacity / 2 - 1;
        mask2 = newCapacity - 1;

        final int oldCapacity = data.length;
        final Object[] oldData = data;

        data = new Object[newCapacity];
        Arrays.fill(data, FREE_KEY);

        size = hasNull ? 1 : 0;

        for (int i = 0; i < oldCapacity; i += 2) {
            final int oldKey = oldData[i];
            if (oldKey != FREE_KEY && oldKey != REMOVED_KEY)
                put(oldKey, (V) oldData[i + 1]);
        }
    }

    public int getStartIndex(final Object key) {
        //key is not null here
        return key.hashCode() & mask;
    }
}
