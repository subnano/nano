package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.ByteString;
import net.nanofix.util.TagBytes;

import java.nio.ByteBuffer;

public class MessageTypeReader implements FIXMessageVisitor {

    protected final MessageHeader messageHeader;
    private String errorMessage;

    public MessageTypeReader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        boolean handled = false;
        // we only care about 35
        if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.MsgType)) {
            if (valueLen == 1) {
                byte msgTypeByte = buffer.get(tagIndex + tagLen + 1);
                messageHeader.msgType(MsgTypeLookup.lookup(msgTypeByte));
                handled = true;
            } else {
                // TODO no -don't create an array here - wasteful
                byte[] msgTypeBytes = ByteBufferUtil.asByteArray(buffer, tagIndex + tagLen + 1, valueLen);
                messageHeader.msgType(new MsgType(ByteString.of(msgTypeBytes).toString()));
                handled = true;
            }
        }
        return handled;
    }

    public MsgType msgType() {
        return messageHeader.msgType();
    }

    @Override
    public void onError(int index, String message) {
        this.errorMessage = message;
    }

    @Override
    public boolean complete() {
        return messageHeader.msgType() != null;
    }
}
