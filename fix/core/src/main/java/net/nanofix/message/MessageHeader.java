package net.nanofix.message;

import io.nano.core.lang.ByteString;

public interface MessageHeader {
    void copyFrom(MessageHeader other);

    ByteString beginString();

    void beginString(ByteString beginString);

    MsgType msgType();

    void msgType(MsgType msgType);

    ByteString senderCompId();

    void senderCompId(ByteString senderCompId);

    ByteString targetCompId();

    void targetCompId(ByteString targetCompId);

    int msgSeqNum();

    void msgSeqNum(int msgSeqNum);

    long sendingTime();

    void sendingTime(long sendingTime);
}
