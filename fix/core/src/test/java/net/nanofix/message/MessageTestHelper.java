package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;

import java.nio.ByteBuffer;

public class MessageTestHelper {
    static void prepareBuffer(ByteBuffer buffer, String msgText) {
        buffer.clear();
        byte[] bytes = FIXMessageStrings.asValidByteArray(msgText);
        ByteBufferUtil.putBytes(buffer, bytes);
    }
}
