package net.nanofix.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class NanoFIXMessageDecoderTest {

    private MessageStringBuilder messageBuilder = new MessageStringBuilder();
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private FIXMessageVisitor decodeHandler;

    @BeforeEach
    void setUp() {
        decodeHandler = spy(new LoggingDecodeHandler());
    }

    @Test
    void junkAtStart() {
        // TODO junkAtStart
    }

    @Test
    void beginStringMissing() {
        MessageTestHelper.prepareBuffer(buffer, "9=12|35=A");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(0, "Message must start with with the correct begin string 8=FIX.");
    }

    @Test
    void beginStringInvalid() {
        MessageTestHelper.prepareBuffer(buffer, "8=NIX.4.0|9=12|35=A");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(0, "Message must start with with the correct begin string 8=FIX.");
    }

    @Test
    void bodyLengthMissing() {
        MessageTestHelper.prepareBuffer(buffer, "8=FIX.4.1|35=A|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) must be the second field in the message");
    }

    @Test
    void bodyLengthTooShort() {
        MessageTestHelper.prepareBuffer(buffer, "8=FIX.4.1|9=1|35=A|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) value is invalid");
    }

    @Test
    void bodyLengthTooLong() {
        MessageTestHelper.prepareBuffer(buffer, "8=FIX.4.1|9=999888777666|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) value is invalid");
    }

    @Test
    @Disabled("May need to change the length validation for this test to pass")
    void bodyLengthInvalidData() {
        MessageTestHelper.prepareBuffer(buffer, "8=FIX.4.1|9=1x|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) should be the second field in the message");
    }

    @Test
    void bodyLengthIncorrect() {
        MessageTestHelper.prepareBuffer(buffer, "8=FIX.4.1|9=18|35=A|49=ACME|");
        decoder.decode(buffer, decodeHandler);
//        verify(decodeHandler)
//                .onError(10,"BodyLength(9) value is invalid");
    }

    @Test
    void msgTypeMissing() {
        MessageTestHelper.prepareBuffer(buffer, "8=FIX.4.1|9=24|49=ACME|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(15, "MsgType(35) must be the third field in the message");
    }

    @Test
    void checksumMissing() {
        // TODO checksumMissing
    }

    @Test
    void checksumIncorrect() {
        // TODO checksumIncorrect
    }

    @Test
    void decodeHeartbeat() {
        String msgText = FIXMessageStrings.HEARTBEAT;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, messageBuilder);
        Assertions.assertThat(messageBuilder.asString()).isEqualTo(msgText);
    }

    @Test
    void decodeLogon() {
        // TODO decodeLogon
    }

    @Test
    void decodeMultipleMessages() {
        // TODO decodeMultipleMessages
    }

}