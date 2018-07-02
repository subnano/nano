package net.nanofix.util;

import net.nanofix.message.Tags;

public final class TagBytes {

    public static final byte[] MsgType = ByteArrayUtil.asByteArray(Tags.MsgType);
    public static final byte[] SenderCompID = ByteArrayUtil.asByteArray(Tags.SenderCompID);
    public static final byte[] TargetCompID = ByteArrayUtil.asByteArray(Tags.TargetCompID);
    public static final byte[] MsgSeqNum = ByteArrayUtil.asByteArray(Tags.MsgSeqNum);
    public static final byte[] SendingTime = ByteArrayUtil.asByteArray(Tags.SendingTime);

    private TagBytes() {
        // can't touch this
    }

}
