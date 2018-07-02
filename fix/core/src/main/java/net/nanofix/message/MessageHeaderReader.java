package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.time.UtcDateTimeDecoder;
import net.nanofix.util.ByteString;
import net.nanofix.util.TagBytes;

import java.nio.ByteBuffer;

public class MessageHeaderReader extends MessageHeader implements FIXMessageVisitor {

    //MessageTypeReader messageTypeReader = new MessageTypeReader();

    @Override
    public void onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        int valueIndex = tagIndex + tagLen + 1;
        if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.MsgSeqNum)) {
            int msgSeqNum = ByteBufferUtil.toInt(buffer, valueIndex, valueLen);
            super.msgSeqNum(msgSeqNum);
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.SenderCompID)) {
            byte[] bytes = ByteBufferUtil.asByteArray(buffer, valueIndex, valueLen);
            super.senderCompId(ByteString.of(bytes));
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.TargetCompID)) {
            byte[] bytes = ByteBufferUtil.asByteArray(buffer, valueIndex, valueLen);
            super.targetCompId(ByteString.of(bytes));
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.SendingTime)) {
            long sendingTimeEpoch = UtcDateTimeDecoder.decode(buffer, valueIndex);
            super.sendingTime(sendingTimeEpoch);
        }
    }

    @Override
    public void onError(int index, String message) {
        // NO-OP
    }

    @Override
    public boolean complete() {
        return super.msgSeqNum() > 0
                && super.senderCompId() != null
                && super.targetCompId() != null
                && super.sendingTime() > 0;
    }
}
