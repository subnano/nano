package net.nanofix.message;

import java.nio.ByteBuffer;

public class LogonMessageReader extends MessageHeaderReader implements FIXMessageVisitor {

    private final MessageHeader messageHeader;

    public LogonMessageReader() {
        this.messageHeader = new MessageHeader();
    }

    public MessageHeader header() {
        return this.messageHeader;
    }

    @Override
    public void onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        if (super.complete()) {
            // populate here
        } else {
            super.onTag(buffer, tagIndex, tagLen, valueLen);
        }
    }

    @Override
    public void onError(int index, String message) {

    }

    @Override
    public boolean complete() {
        return false;
    }
}
