package net.nanofix.message;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class LogonMessageReaderAssert extends AbstractAssert<LogonMessageReaderAssert, LogonMessageReader> {

    private final MessageHeaderAssert messageHeaderAssert;
    //extends MessageHeaderAssert {

    private LogonMessageReaderAssert(LogonMessageReader actual) {
        super(actual, LogonMessageReaderAssert.class);
        this.messageHeaderAssert = new MessageHeaderAssert(actual);
    }

    public static LogonMessageReaderAssert assertThat(LogonMessageReader actual) {
        return new LogonMessageReaderAssert(actual);
    }

    public MessageHeaderAssert header() {
        return messageHeaderAssert;
    }

    public LogonMessageReaderAssert hasEncryptMethod(boolean expected) {
        Assertions.assertThat(actual.encryptMethod()).as("EncryptMethod").isEqualTo(expected);
        return this;
    }

    public LogonMessageReaderAssert hasHeartBeatInterval(int expected) {
        Assertions.assertThat(actual.heartBeatInterval()).as("HeartBeatInterval").isEqualTo(expected);
        return this;
    }

    public LogonMessageReaderAssert hasResetSeqNumFlag(boolean expected) {
        Assertions.assertThat(actual.resetSeqNumFlag()).as("ResetSeqNumFlag").isEqualTo(expected);
        return this;
    }

}
