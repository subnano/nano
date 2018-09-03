package net.nanofix.util;

import io.nano.core.util.ByteArrayUtil;
import net.nanofix.message.Tags;

public final class TagBytes {

    // TODO possible consider a contiguous byte array with offsets & lengths per tag
    public static final byte[] BeginString = ByteArrayUtil.asByteArray(Tags.BeginString);
    public static final byte[] BodyLength = ByteArrayUtil.asByteArray(Tags.BodyLength);
    public static final byte[] MsgType = ByteArrayUtil.asByteArray(Tags.MsgType);
    public static final byte[] SenderCompID = ByteArrayUtil.asByteArray(Tags.SenderCompID);
    public static final byte[] TargetCompID = ByteArrayUtil.asByteArray(Tags.TargetCompID);
    public static final byte[] MsgSeqNum = ByteArrayUtil.asByteArray(Tags.MsgSeqNum);
    public static final byte[] SendingTime = ByteArrayUtil.asByteArray(Tags.SendingTime);
    public static final byte[] EncryptMethod = ByteArrayUtil.asByteArray(Tags.EncryptMethod);
    public static final byte[] HeartBtInt = ByteArrayUtil.asByteArray(Tags.HeartBtInt);
    public static final byte[] ResetSeqNumFlag = ByteArrayUtil.asByteArray(Tags.ResetSeqNumFlag);

    private TagBytes() {
        // can't touch this
    }

}
