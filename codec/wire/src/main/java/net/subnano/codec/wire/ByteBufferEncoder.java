package net.subnano.codec.wire;

import java.nio.ByteBuffer;

public class ByteBufferEncoder<T> implements BufferEncoder {

    private final ByteBuffer buffer;

    public ByteBufferEncoder(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void addByte(byte value) {

    }

    @Override
    public void addShort(int value) {

    }

    @Override
    public void addInt(int value) {

    }

    @Override
    public void addLong(long value) {

    }

    @Override
    public void addString(String value) {

    }
}
