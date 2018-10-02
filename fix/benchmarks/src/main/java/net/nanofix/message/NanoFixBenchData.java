package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.util.ByteArrayUtil;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

public class NanoFixBenchData {

    static final String LOGON = "8=FIX.4.0|9=61|35=A|34=1|49=BANZAI|52=20120331-10:25:15|56=EXEC|98=0|108=30|10=255|";
    static final String MD_SNAPSHOT = "8=FIX.4.2|9=590|35=W|49=LMAXDG-MD|56=B2C2digitalmdMTF3|34=1407781|52=20180926-07:01:40.019|22=8|48=5006|55=XRP/USD|207=LMAX|262=lmaxXRP/USD78da7c662f0948c0|268=13|269=0|270=0.51980|271=8000|290=1|269=0|270=0.51950|271=4000|290=2|269=0|270=0.51890|271=80000|290=3|269=0|270=0.51740|271=250000|290=4|269=0|270=0.51610|271=300000|290=5|269=0|270=0.51520|271=500000|290=6|269=1|270=0.52150|271=4000|290=1|269=1|270=0.52190|271=8000|290=2|269=1|270=0.52210|271=80000|290=3|269=1|270=0.52340|271=250000|290=4|269=1|270=0.52430|271=300000|290=5|269=1|270=0.52520|271=500000|290=6|269=1|270=0.75000|271=2460|290=7|10=143|";

    static ByteBuffer createBuffer(String msgString) {
        ByteBuffer buffer = ByteBuffer.allocate(msgString.length());
        ByteBufferUtil.putBytes(buffer, ByteArrayUtil.asByteArray(msgString.replaceAll("\\|", String.valueOf((char) FIXBytes.SOH))));
        return buffer;
    }

}
