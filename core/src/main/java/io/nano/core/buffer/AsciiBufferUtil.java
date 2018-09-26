package io.nano.core.buffer;

import io.nano.core.util.Maths;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utility class used when encoding Ascii data in ByteBuffers.
 *
 * Ascii here means than numbers are encoded as human readable bytes using US_ASCII Charset.
 *
 * For example, the number 12345 is encoded as the five bytes '12345' rather than an integer using putInt()
 *
 * @author Mark Wardell
 */
public final class AsciiBufferUtil {

    public static final Charset ASCII_CHARSET = StandardCharsets.US_ASCII;

    private AsciiBufferUtil() {
        // can't touch this
    }

    /**
     * Relative <i>put</i> method to copy characters from a {@link CharSequence} into a {@link ByteBuffer}
     *
     * <p>Writes the Ascii bytes from the given sequence into this buffer at the current
     * buffer position, and then increments the position.</p>
     *
     * @param value The CharSequence to be written.
     * @param buffer The target {@link ByteBuffer} to write into
     * @return The number of bytes written
     */
    public static int putCharSequence(CharSequence value, ByteBuffer buffer) {
        int len = putCharSequence(value, buffer, buffer.position());
        buffer.position(buffer.position() + len);
        return len;
    }

    /**
     * Absolute <i>put</i> method to copy characters from a {@link CharSequence} into a {@link ByteBuffer}
     *
     * <p>Writes the Ascii bytes from the given sequence into this buffer at the given offset.</p>
     *
     * @param value The CharSequence to be written.
     * @param buffer The target {@link ByteBuffer} to write the bytes into
     * @param offset The offset at which the bytes will be written
     * @return The number of bytes written
     */
    public static int putCharSequence(CharSequence value, ByteBuffer buffer, int offset) {
        int len = value == null ? 0 : value.length();
        for (int i = 0; i < len; i++) {
            buffer.put(offset + i, (byte) value.charAt(i));
        }
        return len;
    }

    /**
     * Relative <i>put</i> method to copy characters from an {@code int} into a {@link ByteBuffer}
     *
     * <p>Writes the Ascii bytes from the given {@code int} into this buffer at the current
     * buffer position, and then increments the position.</p>
     *
     * @param value The {@code int} to be written.
     * @param buffer The target {@link ByteBuffer} to write into
     * @return The number of bytes written
     */
    public static int putInt(int value, ByteBuffer buffer) {
        int numDigits = Maths.numberDigits(value);
        putInt(value, numDigits, buffer, buffer.position());
        buffer.position(buffer.position() + numDigits);
        return numDigits;
    }

    /**
     * Absolute <i>put</i> method to copy characters from an {@code int} into a {@link ByteBuffer}
     *
     * <p>Writes the Ascii bytes from the given {@code int} into this buffer at the given offset.</p>
     *
     * @param value The {@code int} to be written.
     * @param buffer The target {@link ByteBuffer} to write the bytes into
     * @param offset The offset at which the bytes will be written
     * @return The number of bytes written
     */
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

    /**
     * Absolute <i>put</i> method to copy characters from a {@link long} into a {@link ByteBuffer}
     *
     * <p>Writes the Ascii bytes from the given {@link long} into this buffer at the current
     * buffer position, and then increments the position.</p>
     *
     * @param value The {@link long} to be written.
     * @param buffer The target {@link ByteBuffer} to write into
     * @return The number of bytes written
     */
    public static int putLong(long value, ByteBuffer buffer) {
        int numDigits = Maths.numberDigits(value);
        putLong(value, numDigits, buffer, buffer.position());
        buffer.position(buffer.position() + numDigits);
        return numDigits;
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

    /**
     * Utility method to extract, and create, a String from the given buffer.
     *
     * <p><b>Note:</b> Use with caution as this method allocates objects which is unkind to the GC.</p>
     * @param buffer The source {@link ByteBuffer} to read from
     * @param offset The offset at which the bytes will be read
     * @param len The number of bytes to be read
     * @return A new String instance created from the specified bytes
     */
    public static String getString(ByteBuffer buffer, int offset, int len) {
        byte[] bytes = ByteBufferUtil.asByteArray(buffer, offset, len);
        return new String(bytes, ASCII_CHARSET);
    }

    public static int getInt(ByteBuffer buffer, int offset, int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("Positive length expected");
        }
        int number = 0;
        int index;
        for (int i = 0; i < len; i++) {
            index = offset + len - i - 1;
            number += ((buffer.get(index) - '0') * Maths.pow10(i));
        }
        return number;
    }

    public static long getLong(ByteBuffer buffer, int offset, int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("Positive length expected");
        }
        boolean positive = true;
        long number = 0L;
        long index;
        for (long i = 0; i < len; i++) {
            index = offset + len - i - 1;
            number += ((buffer.get((int) index) - '0') * Maths.pow10(i));
        }
        return positive ? number : 0-number;
    }

}
