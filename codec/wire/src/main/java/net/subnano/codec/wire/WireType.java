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

    public static final int MAX_STRING_LEN = 255;
    public static final int MAX_TEXT_LEN = 65535;

    public static final byte BYTE = 0;
    public static final byte SHORT = 1;
    public static final byte INT = 2;
    public static final byte LONG = 3;
    public static final byte DOUBLE = 4;
    public static final byte STRING = 5;
    public static final byte TEXT = 6;
    public static final byte BYTE_ARRAY = 7;
    public static final byte SHORT_ARRAY = 8;
    public static final byte INT_ARRAY = 9;
    public static final byte LONG_ARRAY = 10;
    public static final byte DOUBLE_ARRAY = 11;

    public static final byte BYTE_SIZE = 1;
    public static final byte SHORT_SIZE = 2;
    public static final byte INT_SIZE = 4;
    public static final byte LONG_SIZE = 8;
    public static final byte DOUBLE_SIZE = 8;
    public static final byte STRING_SIZE = -1;
    public static final byte TEXT_SIZE = -1;

    public static final byte MAX_TYPE = DOUBLE_ARRAY + 1;

    private static byte[] allTypes = new byte[MAX_TYPE + 1];

    private static String[] names = new String[MAX_TYPE + 1];
    private static int[] sizes = new int[MAX_TYPE + 1];
    static {
        allTypes[0] = BYTE;
        allTypes[1] = SHORT;
        allTypes[2] = INT;
        allTypes[3] = LONG;
        allTypes[4] = DOUBLE;
        allTypes[5] = STRING;
        allTypes[6] = TEXT;

        names[BYTE] = "Byte";
        names[SHORT] = "Short";
        names[INT] = "Int";
        names[LONG] = "Long";
        names[DOUBLE] = "Double";
        names[STRING] = "String";
        names[TEXT] = "Text";
        names[BYTE_ARRAY] = "ByteArray";

        sizes[BYTE] = BYTE_SIZE;
        sizes[SHORT] = SHORT_SIZE;
        sizes[INT] = INT_SIZE;
        sizes[LONG] = LONG_SIZE;
        sizes[DOUBLE] = DOUBLE_SIZE;
        sizes[STRING] = STRING_SIZE;
        sizes[TEXT] = TEXT_SIZE;
    }

    private WireType() {
        // can't touch this
    }

    public static String name(int type) {
        if (type > MAX_TYPE)
            throw new IllegalArgumentException("Invalid wire type");
        return names[type];
    }

    public static byte[] values() {
        return allTypes;
    }

    public static int sizeOf(int type) {
        if (type > MAX_TYPE)
            throw new IllegalArgumentException("Invalid wire type");
        return sizes[type];
    }
}
