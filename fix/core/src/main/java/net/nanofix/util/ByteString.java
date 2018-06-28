package net.nanofix.util;

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

//    @Override
//    public String toString() {
//        return "ByteString(" + length() + ')';
//    }
}
