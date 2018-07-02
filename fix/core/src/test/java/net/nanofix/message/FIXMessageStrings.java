package net.nanofix.message;

import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.FIXBytes;

public class FIXMessageStrings {

    static final String LOGON = "8=FIX.4.0|9=61|35=A|34=1|49=BANZAI|52=20120331-10:25:15|56=EXEC|98=0|108=30|10=255|";
    //static final String HEARTBEAT = "8=FIX.4.3|9=72|35=0|49=CLIENT|56=BROKER|34=11|52=19700101-00:00:00.000|112=test-req-id|10=109|";
    static final String HEARTBEAT = "8=FIX.4.2|9=53|35=0|34=2|49=EXEC|52=20120331-10:25:46.426|56=BANZAI|10=167|";


    static byte[] asValidByteArray(String msgString) {
        return ByteArrayUtil.asByteArray(msgString.replaceAll("\\|", String.valueOf((char) FIXBytes.SOH)));
    }
}
