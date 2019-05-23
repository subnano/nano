package net.subnano.codec.wire;

import io.nano.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.ByteBuffer;

import static net.subnano.codec.wire.EncoderTestHelper.verifyData;
import static org.mockito.Mockito.verify;

/**
 * TODO add tests for null strings
 * TODO add tests that exceed maximum string/text length
 */
public class NanoEncoderTest {

    private ByteBuffer buffer;
    private NanoEncoder encoder;

    @Mock
    private WireVisitor wireVisitor;
    private BufferReader bufferReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        buffer  = ByteBuffer.allocate(655360);
        encoder = new NanoEncoder(new ByteBufferEncoder(buffer));
        bufferReader = new ByteBufferReader(buffer);
    }

    @Test
    void addByte() {
        encoder.addByte((byte) 11);
        verifyTagValue(WireType.BYTE, 2, 1, (byte) 11);
    }

    @Test
    void addMaxByte() {
        encoder.addByte( (byte) 255);
        verifyTagValue(WireType.BYTE, 2, 1, (byte) 255);
    }

    @Test
    void addShort() {
        encoder.addShort((short) 22);
        verifyTagValue(WireType.SHORT, 2, 2, (short) 22);
    }

    @Test
    void addMaxShort() {
        encoder.addShort((short) 65535);
        verifyTagValue(WireType.SHORT, 2, 2, (short) 65535);
    }

    @Test
    void addInt() {
        encoder.addInt(33);
        verifyTagValue(WireType.INT, 2, 4, 33);
    }

    @Test
    void addMaxInt() {
        encoder.addInt(Integer.MAX_VALUE);
        verifyTagValue(WireType.INT, 2, 4, Integer.MAX_VALUE);
    }

    @Test
    void addLong() {
        encoder.addLong(44L);
        verifyTagValue(WireType.LONG, 2, 8, 44L);
    }

    @Test
    void addMaxLong() {
        encoder.addLong(Long.MAX_VALUE);
        verifyTagValue(WireType.LONG, 2, 8, Long.MAX_VALUE);
    }

    @Test
    void addDouble() {
        encoder.addDouble(55.5);
        verifyTagValue(WireType.DOUBLE, 2, 8, 55.5);
    }

    @Test
    void addString() {
        String value = Strings.repeat("X", 255);
        encoder.addString(value);
        verifyTagValue(WireType.STRING, 3, value.length(), value);
    }

    @Test
    void addText() {
        String value = Strings.repeat("X", 65535);
        encoder.addText(value);
        verifyTagValue(WireType.TEXT, 4, value.length(), value);
    }

    private void verifyTagValue(
            byte expectedType, int expectedOffset, int expectedLen, Object expectedData) {
        NanoDecoder decoder = new NanoDecoder();
        decoder.parse(bufferReader, wireVisitor);
        verify(wireVisitor).onTag(bufferReader, expectedType, expectedOffset, expectedLen);
        verifyData(bufferReader, expectedType, expectedOffset, expectedLen, expectedData);
    }
}