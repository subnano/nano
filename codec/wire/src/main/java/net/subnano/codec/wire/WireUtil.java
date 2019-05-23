package net.subnano.codec.wire;

public final class WireUtil {

    private static final int TAG_BITS = 12;

    private WireUtil() {
        // can't touch this
    }

    /**
     * Encoded the tag number and wire type into 16 bits
     * @param type
     * @param tag
     * @return
     */
    public static short encodeTag(byte type, int tag) {
        return (short)((type << TAG_BITS) | (tag & 0xfff));
    }

    public static int decodeTag(int packed) {
        return packed & 0xfff;
    }

    public static byte decodeType(int packed) {
        return (byte) ((packed >>> TAG_BITS) & 0xf);
    }
}
