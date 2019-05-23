package net.subnano.codec.wire;

import io.nano.core.buffer.AsciiBufferUtil;

import java.nio.ByteBuffer;

public class ByteBufferReader implements BufferReader {

    private final ByteBuffer buffer;

    public ByteBufferReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int position() {
        return buffer.position();
    }

    @Override
    public byte readByte(int offset) {
        return (byte) (buffer.get(offset) & 0xff);
    }

    @Override
    public byte readByte() {
        return (byte) (buffer.get() & 0xff);
    }

    @Override
    public short readShort(int offset) {
        return buffer.getShort(offset);
    }

    @Override
    public int readInt(int offset) {
        return buffer.getInt(offset);
    }

    @Override
    public long readLong(int offset) {
        return buffer.getLong(offset);
    }

    @Override
    public long readLong() {
        return buffer.getLong();
    }

    @Override
    public double readDouble(int offset) {
        return buffer.getDouble(offset);
    }

    @Override
    public String readString(int offset, int len) {
        // return null or ""  - this why different type for null?
        return AsciiBufferUtil.getString(buffer, offset, len);
    }

}
