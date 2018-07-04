package io.nano.core.util;

public final class Bits {

    //taken from FastUtil
    private static final int INT_PHI = 0x9E3779B9;

    private Bits() {
        // can't touch this
    }

    public static int shuffle(final int a) {
        final int n = a * INT_PHI;
        return n ^ (n >> 16);
    }
}
