package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.ByteString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static net.nanofix.util.FIXBytes.PIPE;
import static net.nanofix.util.FIXBytes.SOH;

class NanoFIXMessageTest {

    private static final ByteString SENDER_COMP_ID = ByteString.of("CLIENT");
    private static final ByteString TARGET_COMP_ID = ByteString.of("BROKER");
    private static final long SENDING_TIME = System.currentTimeMillis();

    private final MessageHeader header = new NanoMessageHeader();
    private final ByteBuffer messageBuffer = ByteBuffer.allocate(256);
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final FIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private FIXMessageVisitor visitor = new LoggingDecodeHandler();

    @BeforeEach
    void setUp() {
        buffer.clear();
    }

    @Test
    void logonMessage() {
        FIXMessage msg = new NanoFIXMessage(messageBuffer);
        msg.header().beginString(BeginStrings.FIX_4_2);
        msg.header().msgType(MsgTypes.Logon);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(42);
        msg.header().sendingTime(SENDING_TIME);
        msg.addIntField(Tags.EncryptMethod, 0);
        msg.addIntField(Tags.HeartBtInt, 30);
        msg.addBooleanField(Tags.ResetSeqNumFlag, true);
        msg.addStringField(Tags.Username, ByteString.of("user1"));
//        TagVisitor visitor = new LocalTagVisitor();
        assertBufferEncoding(
                msg,
                "8=FIX.4.2|9=84|35=A|49=CLIENT|56=BROKER|34=42|52=19700101-00:00:00.000|98=0|" +
                        "108=30|141=Y|553=user1|10=148|"
        );
    }

    @Test
    void heartbeatMessage() {
        FIXMessage msg = new NanoFIXMessage(buffer);
        msg.header().beginString(BeginStrings.FIX_4_3);
        msg.header().msgType(MsgTypes.Heartbeat);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(11);
        msg.addStringField(Tags.TestReqID, ByteString.of("test-req-id"));
//        TagVisitor visitor = new LocalTagVisitor();
        assertBufferEncoding(
                msg,
                "8=FIX.4.3|9=72|35=0|49=CLIENT|56=BROKER|34=11|52=19700101-00:00:00.000|" +
                        "112=test-req-id|10=109|"
        );
    }

    private void assertBufferEncoding(FIXMessage msg, String expected) {
        int length = (int) msg.encode(buffer, 0);
        byte[] bytes = ByteBufferUtil.asByteArray(buffer, 0, length);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        decoder.decode(buffer, visitor);
        ByteArrayUtil.replace(bytes, SOH, PIPE);
        String actualBytesText = ByteArrayUtil.toString(bytes);
        Assertions.assertEquals(expected, actualBytesText);
    }

}