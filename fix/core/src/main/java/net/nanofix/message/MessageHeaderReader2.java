package net.nanofix.message;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.lang.ByteString;
import io.nano.core.time.UtcDateTimeDecoder;
import net.nanofix.util.TagBytes;

import java.nio.ByteBuffer;

public class MessageHeaderReader2 implements FIXMessageVisitor2, MessageHeader {

    private final MessageHeader messageHeader;

    public MessageHeaderReader2(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tag, int valueOffset, int valueLen) {
        boolean handled = true;

        if (tag == Tags.BeginString) {
            // TODO do we need to handle beginString?
        }
        else if (tag == Tags.BodyLength) {
            // TODO do we need to handle bodyLength?
        }
        else if (tag == Tags.MsgType) {
            if (valueLen == 1) {
                byte msgTypeByte = buffer.get(valueOffset);
                messageHeader.msgType(MsgTypeLookup.lookup(msgTypeByte));
            } else {
                // TODO no -don't create an array here - wasteful
                byte[] msgTypeBytes = ByteBufferUtil.asByteArray(buffer, valueOffset, valueLen);
                messageHeader.msgType(new MsgType(ByteString.of(msgTypeBytes).toString()));
            }
        }
        else if (tag == Tags.MsgSeqNum) {
            int msgSeqNum = AsciiBufferUtil.getInt(buffer, valueOffset, valueLen);
            messageHeader.msgSeqNum(msgSeqNum);
        } else if (tag == Tags.SenderCompID) {
            byte[] bytes = ByteBufferUtil.asByteArray(buffer, valueOffset, valueLen);
            messageHeader.senderCompId(ByteString.of(bytes));
        } else if (tag == Tags.TargetCompID) {
            byte[] bytes = ByteBufferUtil.asByteArray(buffer, valueOffset, valueLen);
            messageHeader.targetCompId(ByteString.of(bytes));
        } else if (tag == Tags.SendingTime) {
            long sendingTimeEpoch = UtcDateTimeDecoder.decode(buffer, valueOffset);
            messageHeader.sendingTime(sendingTimeEpoch);
        }
        else {
            handled = false;
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

    boolean isHeaderTag(int tag) {
        return tag == 8 || tag == 9 || tag == 34 || tag == 35 || tag == 49 || tag == 56;
    }

    @Override
    public void clear() {
        messageHeader.senderCompId(null);
        messageHeader.targetCompId(null);
        messageHeader.sendingTime(0);
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

    void onMsgType(ByteBuffer buffer, int valueOffset, int valueLen) {
        byte msgTypeByte = buffer.get(valueOffset);
        messageHeader.msgType(MsgTypeLookup.lookup(msgTypeByte));
    }

    public void onMsgSeqNum(ByteBuffer buffer, int offset, int len) {
        int msgSeqNum = AsciiBufferUtil.getInt(buffer, offset, len);
        messageHeader.msgSeqNum(msgSeqNum);
    }

    public void onSenderCompID(ByteBuffer buffer, int offset, int len) {
        byte[] bytes = ByteBufferUtil.asByteArray(buffer, offset, len);
        messageHeader.senderCompId(ByteString.of(bytes));
    }

    public void onTargetCompID(ByteBuffer buffer, int offset, int len) {
        byte[] bytes = ByteBufferUtil.asByteArray(buffer, offset, len);
        messageHeader.targetCompId(ByteString.of(bytes));
    }

    public void onSendingTime(ByteBuffer buffer, int offset, int len) {
        long sendingTimeEpoch = UtcDateTimeDecoder.decode(buffer, offset);
        messageHeader.sendingTime(sendingTimeEpoch);
    }
}
