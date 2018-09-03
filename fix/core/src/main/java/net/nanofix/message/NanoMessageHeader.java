package net.nanofix.message;

import io.nano.core.util.ByteArrayUtil;
import net.nanofix.util.ByteString;

import java.nio.ByteBuffer;

/**
 * Header
 * 8 BeginString
 * 9 BodyLength
 * 35 MsgType
 * 49 SenderCompID
 * 56 TargetCompID
 * 34 MsgSeqNum
 * 52 SendingTime
 */
public class NanoMessageHeader implements MessageHeader {

    private static final int MAX_FIXED_BUFFER_CAPACITY = 19;
    private static final int LENGTH_OF_SENDING_TIME = 21;

    private ByteString beginString;
    private MsgType msgType;
    private ByteString senderCompId;
    private ByteString targetCompId;
    private int msgSeqNum;
    private long sendingTime;

    @Override
    public void copyFrom(MessageHeader other) {
        this.beginString = other.beginString();
        this.msgType = other.msgType();
        this.senderCompId = other.senderCompId();
        this.targetCompId = other.targetCompId();
        this.msgSeqNum = other.msgSeqNum();
        this.sendingTime = other.sendingTime();
    }

    @Override
    public ByteString beginString() {
        return beginString;
    }

    @Override
    public void beginString(ByteString beginString) {
        this.beginString = beginString;
    }

    @Override
    public MsgType msgType() {
        return msgType;
    }

    @Override
    public void msgType(MsgType msgType) {
        this.msgType = msgType;
    }

    @Override
    public ByteString senderCompId() {
        return senderCompId;
    }

    @Override
    public void senderCompId(ByteString senderCompId) {
        this.senderCompId = senderCompId;
    }

    @Override
    public ByteString targetCompId() {
        return targetCompId;
    }

    @Override
    public void targetCompId(ByteString targetCompId) {
        this.targetCompId = targetCompId;
    }

    @Override
    public int msgSeqNum() {
        return msgSeqNum;
    }

    @Override
    public void msgSeqNum(int msgSeqNum) {
        this.msgSeqNum = msgSeqNum;
    }

    @Override
    public long sendingTime() {
        return sendingTime;
    }

    @Override
    public void sendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
    }

    void encode(final ByteBuffer buffer, final int offset, final int bodyLength) {
        int index = offset;
        //buffer.clear();
        int headerLength = msgType.length()
                + senderCompId.length()
                + targetCompId.length()
                + ByteArrayUtil.lengthOfInt(msgSeqNum)
                + LENGTH_OF_SENDING_TIME;
        int fixBodyLength = headerLength + bodyLength;

//        index += FixBuffer.addStringField(Tags.BeginString, beginString);
//        index += addIntField(Tags.MsgSeqNum, fixBodyLength);
//        addStringField(Tags.MsgType, msgType);
//        addStringField(Tags.SenderCompID, senderCompId);
//        addStringField(Tags.TargetCompID, targetCompId);
//        addIntField(Tags.MsgSeqNum, msgSeqNum);
//        addTimestamp(Tags.SendingTime, sendingTime);
//        return index;
    }

    @Override
    public String toString() {
        return "35=" + msgType + "|"
                + "49=" + new String(senderCompId.bytes()) + "|"
                + "56=" + new String(targetCompId.bytes()) + "|"
                + "34=" + msgSeqNum + "|"
                + "52=" + sendingTime + "|"
                ;
    }
}
