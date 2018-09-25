package io.nano.core.util;

public final class Bits {

    // borrowed from FastUtil
    private static final int INT_PHI = 0x9E3779B9;

    private Bits() {
        // can't touch this
    }

    /**
     * Scrambles bits well enough for a decent key distribution to avoid long array lookup chains
     */
    public static int shuffle(final int a) {
        final int n = a * INT_PHI;
        return n ^ (n >> 16);
    }
}
