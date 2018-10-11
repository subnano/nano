package net.subnano.codec.wire;

public final class WireUtil {

    private WireUtil() {
        // can't touch this
    }

    /**
     * Encoded the tag number and wire type into 16 bits
     * @param type
     * @param tag
     * @return
     */
    public static short  encodeTag(WireType type, int tag) {
        return (short)((type.ordinal() << 12) | (tag & 0xff));
    }
}
