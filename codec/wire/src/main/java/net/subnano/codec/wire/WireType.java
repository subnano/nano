package net.subnano.codec.wire;

/**
 * Notes ...
 * - 4 bits required to encode type
 * - Is char required or can short be used?
 * - Length of String will be 1 byte (255 characters)
 * - Length of LongString will be 2 bytes (65,636)
 * - no other types such as arrays, dates or times have been considered
 * - If longer strings are required then JSON should be considered ;-p
 */
public final class WireType {

    private WireType() {
        // can't touch this
    }

    public static final byte MAX_TYPE = 6;

    public static final byte BYTE = 0;
    public static final byte SHORT = 1;
    public static final byte INT = 2;
    public static final byte LONG = 3;
    public static final byte DOUBLE = 4;
    public static final byte STRING = 5;
    public static final byte TEXT = 6;

    private static byte[] allTypes = new byte[MAX_TYPE + 1];
    private static String[] names = new String[MAX_TYPE + 1];
    static {
        allTypes[0] = BYTE;
        allTypes[1] = SHORT;
        allTypes[2] = INT;
        allTypes[3] = LONG;
        allTypes[4] = DOUBLE;
        allTypes[5] = STRING;
        allTypes[6] = TEXT;

        names[0] = "Byte";
        names[1] = "Short";
        names[2] = "Int";
        names[3] = "Long";
        names[4] = "Double";
        names[5] = "String";
        names[6] = "Text";
    }

    public static String name(int type) {
        return names[type];
    }

    public static byte[] values() {
        return allTypes;
    }
}
