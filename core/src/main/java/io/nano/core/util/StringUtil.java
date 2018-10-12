package io.nano.core.util;

public final class StringUtil {

    private StringUtil() {
        // can't touch this
    }

    /**
     * Returns a string representation of a number rounded to the given number of digits.
     * Negative numbers are currently not supported.
     * @param value double value
     * @param precision precision or decimal places
     * @return string representation of given value
     */
    public static String formatNumber(double value, int precision) {
        // special use cases are when precision is zero or when number has a leading zero
        // TODO move most of this to BAU.putDouble(v,p)
        if (value < 0.0)
            throw new IllegalArgumentException("Negative numbers not supported");
        long scaledNumber = Maths.scaledLong(value, precision);
        boolean lessThanOne = value < 1.0;
        int extraDigits = precision > 0 ? 1 : 0;
        int stringLen = Maths.numberDigits(scaledNumber) + (lessThanOne ? extraDigits + 1 : extraDigits);
        byte[] bytes = new byte[stringLen];
        if (lessThanOne) {
            bytes[0] = '0';
            ByteArrayUtil.putLong(scaledNumber, bytes, 1);
        }
        else {
            ByteArrayUtil.putLong(scaledNumber, bytes, 0);

        }
        if (precision > 0) {
            int pointIndex = stringLen - precision - 1;
            System.arraycopy(bytes, pointIndex, bytes, pointIndex + 1, precision);
            bytes[pointIndex] = '.';
        }
        return ByteArrayUtil.toString(bytes);
    }
}
