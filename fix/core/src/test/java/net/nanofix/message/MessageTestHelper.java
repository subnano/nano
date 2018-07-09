package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

public class MessageTestHelper {

    static ByteBuffer createBuffer(String msgString) {
        ByteBuffer buffer = ByteBuffer.allocate(msgString.length());
        ByteBufferUtil.putBytes(buffer, ByteArrayUtil.asByteArray(
                msgString.replaceAll("\\|", String.valueOf((char) FIXBytes.SOH))));
        buffer.flip();
        buffer.mark();
        return buffer;
    }

    static void prepareBuffer(ByteBuffer buffer, String msgText) {
        buffer.clear();
        byte[] bytes = FIXMessageStrings.asValidByteArray(msgText);
        ByteBufferUtil.putBytes(buffer, bytes);
    }
}
