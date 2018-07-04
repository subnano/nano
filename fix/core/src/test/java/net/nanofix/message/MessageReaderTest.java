package net.nanofix.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class MessageReaderTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private final MessageHeader messageHeader = new NanoMessageHeader();
    private MessageReader messageReader = new MessageReader(messageHeader);

    @Test
    void decodeHeartbeat() {
        String msgText = FIXMessageStrings.HEARTBEAT;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, messageReader);
        Assertions.assertThat(messageHeader.msgType()).isEqualTo(MsgTypes.Heartbeat);
    }
}
