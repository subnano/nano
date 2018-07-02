package net.nanofix.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteString {

    private final byte[] bytes;

    public ByteString(byte[] bytes) {
        this.bytes = bytes;
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
        return bytes == null ? 0 : bytes.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteString that = (ByteString) o;
        return Arrays.equals(bytes, that.bytes);
    }

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
        return "ByteString(" + new String(bytes, StandardCharsets.US_ASCII) + ')';
    }
}
