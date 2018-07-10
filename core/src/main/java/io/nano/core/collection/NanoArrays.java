package io.nano.core.collection;

import io.nano.core.util.Maths;

public final class NanoArrays {

    public static int arraySize(final int expected, final float fillFactor) {
        final long n = Math.max(2, Maths.nextPowerOfTwo((long) Math.ceil(expected / fillFactor)));
        if (n > (1 << 30))
            throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + fillFactor + ")");
        return (int) n;
    }

}
