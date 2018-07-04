package net.nanofix.message;

import java.nio.ByteBuffer;

public class MessageReader implements FIXMessageVisitor {

    private final MessageTypeReader messageTypeReader;

    public MessageReader(MessageHeader messageHeader) {
        this.messageTypeReader = new MessageTypeReader(messageHeader);
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        if (!messageTypeReader.complete())
            messageTypeReader.onTag(buffer, tagIndex, tagLen, valueLen);
        else {
            // determine suitable message reader for MsgType
            messageTypeReader.msgType();
        }
        return true;
    }

    @Override
    public void onError(int index, String message) {

    }

    @Override
    public boolean complete() {
        return false;
    }
}
