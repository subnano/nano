package net.nanofix.message;

import net.nanofix.util.ByteArrayUtil;
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
public class MessageHeader {

    private static final int MAX_FIXED_BUFFER_CAPACITY = 19;
    private static final int LENGTH_OF_SENDING_TIME = 21;

    private ByteString beginString;
    private MsgType msgType;
    private ByteString senderCompId;
    private ByteString targetCompId;
    private int msgSeqNum;
    private long sendingTime;

    public void copyFrom(MessageHeader other) {
        this.beginString = other.beginString;
        this.msgType = other.msgType;
        this.senderCompId = other.senderCompId;
        this.targetCompId = other.targetCompId;
        this.msgSeqNum = other.msgSeqNum;
        this.sendingTime = other.sendingTime;
    }

    public ByteString beginString() {
        return beginString;
    }

    public void beginString(ByteString beginString) {
        this.beginString = beginString;
    }

    public MsgType msgType() {
        return msgType;
    }

    public void msgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public ByteString senderCompId() {
        return senderCompId;
    }

    public void senderCompId(ByteString senderCompId) {
        this.senderCompId = senderCompId;
    }

    public ByteString targetCompId() {
        return targetCompId;
    }

    public void targetCompId(ByteString targetCompId) {
        this.targetCompId = targetCompId;
    }

    public int msgSeqNum() {
        return msgSeqNum;
    }

    public void msgSeqNum(int msgSeqNum) {
        this.msgSeqNum = msgSeqNum;
    }

    public long sendingTime() {
        return sendingTime;
    }

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

}
