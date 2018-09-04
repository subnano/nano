package net.nanofix.message;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.time.UtcDateTimeDecoder;
import net.nanofix.util.ByteString;
import net.nanofix.util.TagBytes;

import java.nio.ByteBuffer;

public class MessageHeaderReader extends MessageTypeReader implements FIXMessageVisitor, MessageHeader {

    public MessageHeaderReader(MessageHeader messageHeader) {
        super(messageHeader);
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        boolean handled = false;

        // possibly let parent handle this tag
        if (!super.complete()) {
            handled = super.onTag(buffer, tagIndex, tagLen, valueLen);
            if (handled) return true;
        }

        // parent not interested - let's have a go
        int valueIndex = tagIndex + tagLen + 1;
        if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.MsgSeqNum)) {
            int msgSeqNum = AsciiBufferUtil.getInt(buffer, valueIndex, valueLen);
            messageHeader.msgSeqNum(msgSeqNum);
            handled = true;
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.SenderCompID)) {
            byte[] bytes = ByteBufferUtil.asByteArray(buffer, valueIndex, valueLen);
            messageHeader.senderCompId(ByteString.of(bytes));
            handled = true;
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.TargetCompID)) {
            byte[] bytes = ByteBufferUtil.asByteArray(buffer, valueIndex, valueLen);
            messageHeader.targetCompId(ByteString.of(bytes));
            handled = true;
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.SendingTime)) {
            long sendingTimeEpoch = UtcDateTimeDecoder.decode(buffer, valueIndex);
            messageHeader.sendingTime(sendingTimeEpoch);
            handled = true;
        }
        return handled;
    }

    @Override
    public void onError(int index, String message) {
        // NO-OP
    }

    @Override
    public boolean complete() {
        return messageHeader.msgSeqNum() > 0
                && messageHeader.senderCompId() != null
                && messageHeader.targetCompId() != null
                && messageHeader.sendingTime() > 0;
    }

    @Override
    public void copyFrom(MessageHeader other) {
        messageHeader.copyFrom(other);
    }

    @Override
    public ByteString beginString() {
        return messageHeader.beginString();
    }

    @Override
    public void beginString(ByteString beginString) {
        messageHeader.beginString(beginString);
    }

    @Override
    public MsgType msgType() {
        return messageHeader.msgType();
    }

    @Override
    public void msgType(MsgType msgType) {
        messageHeader.msgType(msgType);
    }

    @Override
    public ByteString senderCompId() {
        return messageHeader.senderCompId();
    }

    @Override
    public void senderCompId(ByteString senderCompId) {
        messageHeader.senderCompId(senderCompId);
    }

    @Override
    public ByteString targetCompId() {
        return messageHeader.targetCompId();
    }

    @Override
    public void targetCompId(ByteString targetCompId) {
        messageHeader.targetCompId(targetCompId);
    }

    @Override
    public int msgSeqNum() {
        return messageHeader.msgSeqNum();
    }

    @Override
    public void msgSeqNum(int msgSeqNum) {
        messageHeader.msgSeqNum(msgSeqNum);
    }

    @Override
    public long sendingTime() {
        return messageHeader.sendingTime();
    }

    @Override
    public void sendingTime(long sendingTime) {
        messageHeader.sendingTime(sendingTime);
    }
}
