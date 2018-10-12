package net.subnano.codec.wire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class NanoEncoderTest {

    private ByteBuffer buffer;
    private NanoEncoder encoder;

    @BeforeEach
    void setUp() {
        buffer  = ByteBuffer.allocate(256);
        encoder = new NanoEncoder(new ByteBufferEncoder(buffer));
    }

    @Test
    void addByte() {
        encoder.addByte(1, (byte) 11);
        verifyBuffer(buffer);
    }

    @Test
    void addShort() {
        encoder.addShort(2, (short) 22);
        verifyBuffer(buffer);
    }

    @Test
    void addInt() {
        encoder.addInt(3, 33);
        verifyBuffer(buffer);
    }

    @Test
    void addLong() {
        encoder.addLong(4, 44L);
        verifyBuffer(buffer);
    }

    @Test
    void addDouble() {
        encoder.addDouble(5, 55.5);
        verifyBuffer(buffer);
    }

    @Test
    void addString() {
        encoder.addString(6, "String value #6");
        verifyBuffer(buffer);
    }

    private void verifyBuffer(ByteBuffer buffer) {
        // TODO need the decoder to determine buffer created correctly
        System.out.println(buffer);
    }
}