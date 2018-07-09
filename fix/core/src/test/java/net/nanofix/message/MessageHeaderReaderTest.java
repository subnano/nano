package net.nanofix.message;

import net.nanofix.time.UtcDateTimeDecoder;
import net.nanofix.util.ByteString;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

class MessageHeaderReaderTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private MessageHeaderReader reader = new MessageHeaderReader(new NanoMessageHeader());

    @Test
    void decodeMessageHeader() {
        String msgText = FIXMessageStrings.HEARTBEAT;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, reader);
        MessageHeaderAssert.assertThat(reader)
                .hasMsgType(MsgTypes.Heartbeat)
                .hasMsgSeqNum(2)
                .hasSenderCompId(ByteString.of("EXEC"))
                .hasTargetCompId(ByteString.of("BANZAI"))
                .hasSendingTime(UtcDateTimeDecoder.decode("20120331-10:25:46.426"));
    }
}