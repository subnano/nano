package io.nano.core.lang;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.util.ByteArrayUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * {@link ByteString} is designed to be a static wrapper around a byte array.
 *
 * It is not meant to be a reusable buffer space, hence there is no clear/reset functionality.
 *
 * @author Mark Wardell
 */
public class ByteString {

    private final byte[] bytes;
    private final int length;
    private final int hashCode;

    // the constructor is used for derived classes only
    protected ByteString(byte[] bytes) {
        this.bytes = bytes;
        this.length = bytes == null ? 0 : bytes.length;
        this.hashCode = ByteArrayUtil.hash(bytes, 0, length);
    }

    public static ByteString of(String string) {
        return new ByteString(ByteArrayUtil.asByteArray(string));
    }

    public static ByteString of(byte[] bytes) {
        return new ByteString(bytes);
    }

    public byte[] bytes() {
        return bytes;
    }

    public int length() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public byte byteAt(int pos) {
        return bytes[pos];
    }

    // A faster equals for most use cases
    public boolean equals(ByteString that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (that.length != length)
            return false;
        for (int i=0; i<length; i++)
            if (bytes[i] != that.bytes[i])
                return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ByteString that = (ByteString)o;
        return Arrays.equals(bytes, that.bytes);
    }

//    @Override
//    public int hashCode() {
//        return hashCode;
//    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * Do not use toString() if you are concerned about object allocation as this method
     * allocates a new String on every invocation.
     */
    @Override
    public String toString() {
        return ByteArrayUtil.toString(bytes);
    }
}
