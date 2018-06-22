package net.nanofix.util;

public final class FIXBytes {

    public static final byte SOH = (byte) 0x01;
    public static final byte EQUALS = '=';
    public static final byte PIPE = '|';
    public static final byte FIX_TRUE = 'Y';
    public static final byte FIX_FALSE = 'N';
    public static final byte BODY_LEN_TAG = '9';

    public static final byte[] BEGIN_STRING_PREFIX = ByteArrayUtil.asByteArray("8=FIX.");
    public static final byte[] CHECKSUM_PREFIX = ByteArrayUtil.asByteArray("10=");
    public static final byte[] CHECKSUM_PLACEHOLDER = ByteArrayUtil.asByteArray("000");
    public static final byte[] MSG_TYPE_TAG_BYTES = ByteArrayUtil.asByteArray("35");

    public static final byte HANDL_INST_AUTO = '1';
    public static final byte HANDL_INST_AUTO_PREFERRED = '2';
    public static final byte HANDL_INST_MANUAL = '3';

    public static final byte ORD_TYPE_MARKET = '1';
    public static final byte ORD_TYPE_LIMIT = '2';
    public static final byte ORD_TYPE_STOP = '3';
    public static final byte ORD_TYPE_STOP_LIMIT = '4';
    public static final byte ORD_TYPE_STOP_QUOTED = 'D';

    public static final byte SIDE_BUY = '1';
    public static final byte SIDE_SELL = '2';

    public static final byte TIF_DAY = '0';
    public static final byte TIF_GTC = '1';
    public static final byte TIF_OPG = '2';
    public static final byte TIF_IOC = '3';
    public static final byte TIF_FOK = '4';
    public static final byte TIF_GTX = '5';
    public static final byte TIF_GTD = '6';

    private FIXBytes() {
        // can't touch this
    }

}
