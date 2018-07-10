package io.nano.core.collection;

import io.nano.core.util.Bits;

public class NanoIntIntMap implements IntIntMap {

    private static final int FREE_KEY = 0;

    public static final int NO_VALUE = 0;

    /**
     * Keys and values
     */
    private int[] data;

    /**
     * Do we have 'free' key in the map?
     */
    private boolean hasFreeKey;

    /**
     * Value of 'free' key
     */
    private int freeValue;

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
    private int mask2;

    public NanoIntIntMap(final int size, final float fillFactor) {
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
        this.data = new int[capacity * 2];
        this.threshold = (int) (capacity * fillFactor);
    }

    @Override
    public int get(final int key) {
        int ptr = (Bits.shuffle(key) & mask) << 1;

        if (key == FREE_KEY)
            return hasFreeKey ? freeValue : NO_VALUE;

        int k = data[ptr];

        if (k == FREE_KEY)
            return NO_VALUE;  //end of chain already
        if (k == key) //we check FREE prior to this call
            return data[ptr + 1];

        while (true) {
            ptr = (ptr + 2) & mask2; //that's next index
            k = data[ptr];
            if (k == FREE_KEY)
                return NO_VALUE;
            if (k == key)
                return data[ptr + 1];
        }
    }

    @Override
    public int put(final int key, final int value) {
        if (key == FREE_KEY) {
            final int ret = freeValue;
            if (!hasFreeKey)
                ++size;
            hasFreeKey = true;
            freeValue = value;
            return ret;
        }

        int ptr = (Bits.shuffle(key) & mask) << 1;
        int k = data[ptr];
        if (k == FREE_KEY) //end of chain already
        {
            data[ptr] = key;
            data[ptr + 1] = value;
            if (size >= threshold)
                rehash(data.length * 2); //size is set inside
            else
                ++size;
            return NO_VALUE;
        } else if (k == key) //we check FREE prior to this call
        {
            final int ret = data[ptr + 1];
            data[ptr + 1] = value;
            return ret;
        }

        while (true) {
            ptr = (ptr + 2) & mask2; //that's next index calculation
            k = data[ptr];
            if (k == FREE_KEY) {
                data[ptr] = key;
                data[ptr + 1] = value;
                if (size >= threshold)
                    rehash(data.length * 2); //size is set inside
                else
                    ++size;
                return NO_VALUE;
            } else if (k == key) {
                final int ret = data[ptr + 1];
                data[ptr + 1] = value;
                return ret;
            }
        }
    }

    @Override
    public int remove(final int key) {
        if (key == FREE_KEY) {
            if (!hasFreeKey)
                return NO_VALUE;
            hasFreeKey = false;
            --size;
            return freeValue; //value is not cleaned
        }

        int ptr = (Bits.shuffle(key) & mask) << 1;
        int k = data[ptr];
        if (k == key) //we check FREE prior to this call
        {
            final int res = data[ptr + 1];
            shiftKeys(ptr);
            --size;
            return res;
        } else if (k == FREE_KEY)
            return NO_VALUE;  //end of chain already
        while (true) {
            ptr = (ptr + 2) & mask2; //that's next index calculation
            k = data[ptr];
            if (k == key) {
                final int res = data[ptr + 1];
                shiftKeys(ptr);
                --size;
                return res;
            } else if (k == FREE_KEY)
                return NO_VALUE;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int shiftKeys(int pos) {
        // Shift entries with the same hash.
        int last, slot;
        int k;
        final int[] data = this.data;
        while (true) {
            pos = ((last = pos) + 2) & mask2;
            while (true) {
                if ((k = data[pos]) == FREE_KEY) {
                    data[last] = FREE_KEY;
                    return last;
                }
                slot = (Bits.shuffle(k) & mask) << 1; //calculate the starting slot for the current key
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = (pos + 2) & mask2; //go to the next entry
            }
            data[last] = k;
            data[last + 1] = data[pos + 1];
        }
    }

    private void rehash(final int newCapacity) {
        threshold = (int) (newCapacity / 2 * fillFactor);
        mask = newCapacity / 2 - 1;
        mask2 = newCapacity - 1;

        final int oldCapacity = data.length;
        final int[] oldData = data;

        data = new int[newCapacity];
        size = hasFreeKey ? 1 : 0;

        for (int i = 0; i < oldCapacity; i += 2) {
            final int oldKey = oldData[i];
            if (oldKey != FREE_KEY)
                put(oldKey, oldData[i + 1]);
        }
    }

}
