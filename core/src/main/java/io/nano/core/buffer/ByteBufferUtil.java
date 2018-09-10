package io.nano.core.buffer;

import io.nano.core.lang.ByteString;
import io.nano.core.util.ByteArrayUtil;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author Mark Wardell
 */
public final class ByteBufferUtil {

    static final byte[] CHECKSUM_BYTE_SUFFIX = new byte[] {'0', '1', '0', '='};
    static final int NOT_FOUND_INDEX = -1;
    static final String HEX = "0123456789ABCDEF";

    private ByteBufferUtil() {
        // can't touch this
    }

    public static int indexOf(ByteBuffer buffer, int startIndex, byte value) {
        int toIndex = buffer.position();
        for (int index = startIndex; index < toIndex; index++) {
            if (buffer.get(index) == value) {
                return index;
            }
        }
        return NOT_FOUND_INDEX;
    }

    /**
     * Takes the bytes from the array of given ByteBuffers and combines them
     * into a single byte array.
     *
     * @param buffers the collection of ByteBuffers to combine
     * @return a new byte array
     */
    public static byte[] asByteArray(ByteBuffer[] buffers) {
        int bufferLen = 0;
        for (ByteBuffer buffer : buffers) {
            buffer.flip();  // flip to reading mode
            bufferLen += buffer.remaining();
        }
        byte[] bytes = new byte[bufferLen];
        int offset = 0;
        for (ByteBuffer buffer : buffers) {
            int srcLen = buffer.remaining();
            buffer.get(bytes, offset, srcLen);
            offset += srcLen;
            buffer.clear();
        }
        return bytes;
    }

    public static byte[] asByteArray(ByteBuffer buffer, int index, int len) {
        byte[] bytes = new byte[len];
        //buffer.get(bytes);
        if (len > buffer.remaining()) {
            throw new BufferUnderflowException();
        }
        for (int i = 0; i < len; i++) {
            bytes[i] = buffer.get(index + i);
        }
        return bytes;
    }

    /**
     * Returns a new ByteString from the given ByteBuffer at offset and of len.
     */
    public static ByteString asByteString(ByteBuffer buffer, int index, int len) {
        byte[] bytes = asByteArray(buffer, index, len);
        return ByteString.of(bytes);
    }

    /**
     * ByteBuffer contents for a FIX message should end with 010=nnn| (8 bytes)
     */
    public static boolean hasChecksum(ByteBuffer buffer) {
        int offset = buffer.remaining() - 8;
        return ByteBufferUtil.hasBytes(buffer, offset, CHECKSUM_BYTE_SUFFIX);
    }

    /**
     * Checks if the byte in the buffer at position represented by offset
     * matches the given byte.
     */
    public static boolean hasByte(ByteBuffer buffer, int offset, byte wantedByte) {
        return buffer != null && buffer.get(offset) == wantedByte;
    }

    /**
     * Checks whether the bytes in the buffer at position represented by offset
     * are equal to the bytes in the given byte array.
     */
    public static boolean hasBytes(ByteBuffer buffer, int offset, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (buffer.get(offset + i) != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the bytes in the given {@link ByteBuffer} and offset match the contents of the {@link ByteString}
     * @return True if they match, otherwise false.
     */
    public static boolean equals(ByteBuffer buffer, int offset, int len, ByteString byteString) {
        boolean equal = false;
        // don't even start unless the lengths are the same
        if (byteString.length() == len) {
            return hasBytes(buffer, offset, byteString.bytes());
        }
        return equal;
    }

    public static int readableBytes(ByteBuffer buffer) {
        return buffer.position();
    }

    public static int readableBytes(ByteBuffer[] buffers) {
        int readableBytes = 0;
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                if (buffer != null) {
                    readableBytes += buffer.remaining();
                }
            }
        }
        return readableBytes;
    }

    // need to convey a FIX boolean of Y / N
    public static boolean toBoolean(ByteBuffer buffer, int offset, int len) {
        if (len != 1) {
            throw new IllegalArgumentException("Len should be 1 when decoding a boolean");
        }
        byte aByte = buffer.get(offset);
        return aByte == 'Y';
    }

    public static void putBytes(ByteBuffer buffer, byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
    }

    /**
     * Wraps a String {@link String} into a {@link ByteBuffer}
     *
     * <p>Should't really use {@link String} for high performance code but sometime it can't be avoided.</p>
     *
     * @param text The given text to wrap into a {@link ByteBuffer}
     * @return a {@link ByteBuffer} wrapped around the given string
     */
    public static ByteBuffer wrap(final String text) {
        int len = text == null ? 0 : text.length();
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte)text.charAt(i);
        }
        return ByteBuffer.wrap(bytes);
    }

    public static String hexDump(final ByteBuffer buffer) {
        return hexDump(buffer, 0, buffer.limit());
    }

    public static String hexDump(final ByteBuffer buffer, final int offset, final int length) {
        int charsPerRow = 16;
        StringBuilder lineBuilder = new StringBuilder();
        StringBuilder charBuilder = new StringBuilder(charsPerRow);
        int rows = Math.floorDiv(length, charsPerRow);
        for (int row = 0; row <= rows; row++) {
            charBuilder.setLength(0);
            int lineStart = row * charsPerRow;
            int linedEnd = Math.min(lineStart + charsPerRow, length);
            for (int i = lineStart; i < linedEnd; i++) {
                byte b = buffer.get(i);
                lineBuilder.append(toHex(b));
                if (i < linedEnd) {
                    lineBuilder.append(" ");
                }
                charBuilder.append(b == 0 ? "." : new Character((char)b));
            }
            lineBuilder.append("  ").append(charBuilder.toString());
            if (row < rows) {
                lineBuilder.append("\n");
            }
        }
        return lineBuilder.toString();
    }

    public static String toHex(byte b) {
        StringBuilder sb = new StringBuilder(2);
        sb.append(HEX.charAt((b & 0xF0) >> 4));
        sb.append(HEX.charAt(b & 0x0F));
        return sb.toString();
    }

    public static int hash(ByteBuffer buffer, int offset, int len) {
        if (buffer.hasArray()) {
            return ByteArrayUtil.hash(buffer.array(), offset, len);
        }
        throw new UnsupportedOperationException("not implemented for DirectBuffers");
    }

    public static int hash2(ByteBuffer buffer, int offset, int len) {
        if (buffer.hasArray()) {
            return ByteArrayUtil.hash2(buffer.array(), offset, len);
        }
        throw new UnsupportedOperationException("not implemented for DirectBuffers");
    }
}
