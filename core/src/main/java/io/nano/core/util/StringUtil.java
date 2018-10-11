package io.nano.core.util;

public final class StringUtil {

    private StringUtil() {
        // can't touch this
    }

    public static String formatNumber(double value, int precision) {
        // special use cases are when precision is zero or when number has a leading zero
        // TODO handle half up rounding if require or a separate methos
        if (value < 0.0)
            throw new IllegalArgumentException("Negative numbers not supported");
        long scaledNumber = (long) (value * (double) Maths.pow10(precision));
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
