package io.nano.core.util;

public class Maths {

    static final int MAX_INT_EXPONENT_POW10 = 9;
    static final int MAX_LONG_EXPONENT_POW10 = 19;

    static final int[] INT_POWERS_OF_10 = new int[MAX_INT_EXPONENT_POW10 + 1];
    static final long[] LONG_POWERS_OF_10 = new long[MAX_LONG_EXPONENT_POW10 + 1];

    static {
        for (int i = 0; i <= MAX_INT_EXPONENT_POW10; i++) {
            INT_POWERS_OF_10[i] = (int)Math.pow(10, i);
        }
        for (int i = 0; i <= MAX_LONG_EXPONENT_POW10; i++) {
            LONG_POWERS_OF_10[i] = (long)Math.pow(10, i);
        }
    }

    public static int pow10(final int i) {
        if (i >= 0 && i <= MAX_INT_EXPONENT_POW10) {
            return INT_POWERS_OF_10[i];
        }
        throw new IllegalArgumentException("Value not supported");
    }

    public static long pow10(final long i) {
        if (i >= 0 && i <= MAX_LONG_EXPONENT_POW10) {
            return LONG_POWERS_OF_10[(int)i];
        }
        throw new IllegalArgumentException("Value not supported");
    }

    public static long pow(long a, long b) {
        long pow = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                pow *= a;
            }
            b >>= 1;
            a *= a;
        }
        return pow;
    }

    public static int numberDigits(final int n) {
        if (n < 10) {
            return 1;
        }
        if (n < 100) {
            return 2;
        }
        if (n < 1_000) {
            return 3;
        }
        if (n < 10_000) {
            return 4;
        }
        if (n < 100_000) {
            return 5;
        }
        if (n < 1_000_000) {
            return 6;
        }
        if (n < 10_000_000) {
            return 7;
        }
        if (n < 100_000_000) {
            return 8;
        }
        if (n < 1_000_000_000) {
            return 9;
        }
        return 10;
    }

    public static int numberDigits(final long n) {
        return n < 1_000_000_000 ? numberDigits((int)n) : ((int)Math.log10((double)n)) + 1;
    }

    public static boolean isPowerOfTwo(final long n) {
        return (n & (n - 1)) == 0;
    }

    /**
     * Return the least power of two greater than or equal to the specified value.
     *
     * @param x a long integer smaller than or equal to 2<sup>62</sup>.
     * @return the least power of two greater than or equal to the specified value.
     * Returns 1 when input argument is 0.
     */
    public static long nextPowerOfTwo(long x) {
        if (x == 0) {
            return 1;
        }
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }

    /**
     * <p>Returns a scaled long representation of a double value with the given precision using HALF_UP rounding.</p>
     *
     * <p>E.g. {@code scaledLong(8.12345, 2)} returns 812</p>
     *
     * @param value double value
     * @param precision precision or decimal places
     * @return scaled long
     */
    public static long scaledLong(double value, int precision) {
        // TODO add explicit rounding modes should the consumer wish to use HALF_EVEN
        long longValue = (long)(value * (double)Maths.pow10(precision));
        long nextDigit = (long)(value * (double)Maths.pow10(precision + 1)) % 10;
        long adjustment = nextDigit >= 5 ? 10 - nextDigit : -nextDigit;
        return longValue + adjustment;
    }
}
