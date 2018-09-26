package net.nanofix.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class MarketDataDecoderTest {

    // Snapshot length is 614 bytes
    private static final String SNAPSHOT = "8=FIX.4.2|9=590|35=W|49=LMAXDG-MD|56=B2C2digitalmdMTF3|34=1407781|52=20180926-07:01:40.019|22=8|48=5006|55=XRP/USD|207=LMAX|262=lmaxXRP/USD78da7c662f0948c0|268=13|269=0|270=0.51980|271=8000|290=1|269=0|270=0.51950|271=4000|290=2|269=0|270=0.51890|271=80000|290=3|269=0|270=0.51740|271=250000|290=4|269=0|270=0.51610|271=300000|290=5|269=0|270=0.51520|271=500000|290=6|269=1|270=0.52150|271=4000|290=1|269=1|270=0.52190|271=8000|290=2|269=1|270=0.52210|271=80000|290=3|269=1|270=0.52340|271=250000|290=4|269=1|270=0.52430|271=300000|290=5|269=1|270=0.52520|271=500000|290=6|269=1|270=0.75000|271=2460|290=7|10=143|";

    private MessageStringBuilder messageBuilder = new MessageStringBuilder();
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    @Test
    void decodeMarketDataSnapshot() {
        String msgText = SNAPSHOT;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, messageBuilder);
        Assertions.assertThat(messageBuilder.asString()).isEqualTo(msgText);

        // TODO Replace MessageStringBuilder with a likely implementation for benchmarking
        // TODO Code and benchmark to include MsgType switching
    }

}
