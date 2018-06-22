package net.nanofix.message;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class DecodeMessagesTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder(false);
    private ByteBuffer headerBuffer = ByteBuffer.allocate(256);
    private MessageHeaderAdapter messageHeader = new MessageHeaderAdapter(new MessageHeader(headerBuffer));

    @Test
    void decodeLogon() {
        String msgText = FIXMessageStrings.HEARTBEAT;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, messageHeader);

    }
}
