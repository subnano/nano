package net.nanofix.message;

import net.nanofix.util.ByteString;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class MessageHeaderAssert extends AbstractAssert<MessageHeaderAssert, MessageHeader> {

    protected MessageHeaderAssert(MessageHeader actual) {
        super(actual, MessageHeaderAssert.class);
    }

    public static MessageHeaderAssert assertThat(MessageHeader actual) {
        return new MessageHeaderAssert(actual);
    }

    public MessageHeaderAssert hasMsgType(MsgType expected) {
        Assertions.assertThat(actual.msgType()).as("MsgType").isEqualTo(expected);
        return this;
    }

    public MessageHeaderAssert hasMsgSeqNum(int expected) {
        Assertions.assertThat(actual.msgSeqNum()).as("MsgSeqNum").isEqualTo(expected);
        return this;
    }

    public MessageHeaderAssert hasSenderCompId(ByteString expected) {
        Assertions.assertThat(actual.senderCompId()).as("SenderCompId").isEqualTo(expected);
        return this;
    }

    public MessageHeaderAssert hasTargetCompId(ByteString expected) {
        Assertions.assertThat(actual.targetCompId()).as("TargetCompId").isEqualTo(expected);
        return this;
    }

    public MessageHeaderAssert hasSendingTime(long expected) {
        Assertions.assertThat(actual.sendingTime()).as("SendingTime").isEqualTo(expected);
        return this;
    }

}
