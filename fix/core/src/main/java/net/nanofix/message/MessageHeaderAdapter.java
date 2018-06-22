package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

public class MessageHeaderAdapter implements MessageDecodeHandler {

    private final MessageHeader messageHeader;

    public MessageHeaderAdapter(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    @Override
    public void onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        // we only care about 35
        if (ByteBufferUtil.hasBytes(buffer, tagIndex, FIXBytes.MSG_TYPE_TAG_BYTES)) {
            messageHeader.msgType(msgType);
        }
    }

    @Override
    public void onError(int index, String message) {

    }
}
