package io.nano.core.buffer;

import io.nano.core.util.Maths;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utility class used when encoding Ascii data in ByteBuffers.
 * Ascii here means than numbers are encoded as human readable bytes using US_ASCII Charset.
 *
 * @author Mark Wardell
 */
public final class AsciiBufferUtil {

    public static final Charset ASCII_CHARSET = StandardCharsets.US_ASCII;

    private AsciiBufferUtil() {
        // can't touch this
    }

    public static int putInt(int value, ByteBuffer buffer) {
        int numDigits = Maths.numberDigits(value);
        return putInt(value, numDigits, buffer, buffer.position());
    }

    public static int putInt(int value, ByteBuffer buffer, int offset) {
        int numDigits = Maths.numberDigits(value);
        return putInt(value, numDigits, buffer, offset);
    }

    public static int putInt(int value, int len, ByteBuffer buffer, int offset) {
        for (int i = 0; i < len; i++) {
            int power = Maths.pow10(len - i - 1);
            byte digit = (byte) Math.floorDiv(value, power);
            buffer.put(offset + i, (byte) (digit + '0'));
            value -= (digit * power);
        }
        // 2147483647 (10) - 1000000000 (10)
        return len;
    }

    public static int putLong(long value, ByteBuffer buffer) {
        int numDigits = Maths.numberDigits(value);
        return putLong(value, numDigits, buffer, buffer.position());
    }

    public static int putLong(long value, ByteBuffer buffer, int offset) {
        int numDigits = Maths.numberDigits(value);
        return putLong(value, numDigits, buffer, offset);
    }

    public static int putLong(long value, int len, ByteBuffer buffer, int offset) {
        for (int i = 0; i < len; i++) {
            long power = Maths.pow10((long) len - i - 1);
            byte digit = (byte) Math.floorDiv(value, power);
            buffer.put(offset + i, (byte) (digit + '0'));
            value -= (digit * power);
        }
        return len;
    }

    public static String getString(ByteBuffer buffer, int offset, int len) {
        byte[] bytes = ByteBufferUtil.asByteArray(buffer, offset, len);
        return new String(bytes, ASCII_CHARSET);
    }
}
