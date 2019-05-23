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
public class NanoTagEncoderTest {

    private ByteBuffer buffer;
    private NanoTagEncoder encoder;

    @Mock
    private TagVisitor tagVisitor;
    private BufferReader bufferReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        buffer  = ByteBuffer.allocate(655360);
        encoder = new NanoTagEncoder(new ByteBufferEncoder(buffer));
        bufferReader = new ByteBufferReader(buffer);
    }

    @Test
    void addByte() {
        encoder.addByte(1, (byte) 11);
        verifyTagValue(1, WireType.BYTE, 2, 1, (byte) 11);
    }

    @Test
    void addMaxByte() {
        encoder.addByte(1, (byte) 255);
        verifyTagValue(1, WireType.BYTE, 2, 1, (byte) 255);
    }

    @Test
    void addShort() {
        encoder.addShort(2, (short) 22);
        verifyTagValue(2, WireType.SHORT, 2, 2, (short) 22);
    }

    @Test
    void addMaxShort() {
        encoder.addShort(2, (short) 65535);
        verifyTagValue(2, WireType.SHORT, 2, 2, (short) 65535);
    }

    @Test
    void addInt() {
        encoder.addInt(3, 33);
        verifyTagValue(3, WireType.INT, 2, 4, 33);
    }

    @Test
    void addMaxInt() {
        encoder.addInt(3, Integer.MAX_VALUE);
        verifyTagValue(3, WireType.INT, 2, 4, Integer.MAX_VALUE);
    }

    @Test
    void addLong() {
        encoder.addLong(4, 44L);
        verifyTagValue(4, WireType.LONG, 2, 8, 44L);
    }

    @Test
    void addMaxLong() {
        encoder.addLong(4, Long.MAX_VALUE);
        verifyTagValue(4, WireType.LONG, 2, 8, Long.MAX_VALUE);
    }

    @Test
    void addDouble() {
        encoder.addDouble(5, 55.5);
        verifyTagValue(5, WireType.DOUBLE, 2, 8, 55.5);
    }

    @Test
    void addString() {
        String value = Strings.repeat("X", 255);
        encoder.addString(6, value);
        verifyTagValue(6, WireType.STRING, 3, value.length(), value);
    }

    @Test
    void addText() {
        String value = Strings.repeat("X", 65535);
        encoder.addText(6, value);
        verifyTagValue(6, WireType.TEXT, 4, value.length(), value);
    }

    private void verifyTagValue(int expectedTag, byte expectedType, int expectedOffset, int expectedLen, Object expectedData) {
        NanoTagDecoder decoder = new NanoTagDecoder();
        decoder.decode(bufferReader, tagVisitor);
        verify(tagVisitor).onTag(bufferReader, expectedTag, expectedType, expectedOffset, expectedLen);
        verifyData(bufferReader, expectedType, expectedOffset, expectedLen, expectedData);
    }
}