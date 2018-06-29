package net.nanofix.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class MessageTypeReaderTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private final MessageHeader messageHeader = new MessageHeader();
    private MessageTypeReader messageTypeReader = new MessageTypeReader(messageHeader);

    @Test
    void decodeMsgType() {
        String msgText = FIXMessageStrings.HEARTBEAT;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, messageTypeReader);
        Assertions.assertThat(messageHeader.msgType()).isEqualTo(MsgTypes.Heartbeat);
    }

}
