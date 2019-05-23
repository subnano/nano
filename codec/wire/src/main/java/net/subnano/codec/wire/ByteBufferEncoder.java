package net.subnano.codec.wire;

import io.nano.core.lang.ByteString;

import java.nio.ByteBuffer;

public class ByteBufferEncoder implements BufferEncoder {

    private final ByteBuffer buffer;

    public ByteBufferEncoder(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void addByte(byte value) {
        buffer.put(value);
    }

    @Override
    public void addShort(short value) {
        buffer.putShort(value);
    }

    @Override
    public void addInt(int value) {
        buffer.putInt(value);
    }

    @Override
    public void addLong(long value) {
        buffer.putLong(value);
    }

    @Override
    public void addDouble(double value) {
        buffer.putDouble(value);
    }

    @Override
    public void addBytes(byte[] value) {
        buffer.put(value);
    }
}
