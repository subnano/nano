package net.subnano.codec.wire;

public interface BufferEncoder {

    void addByte(byte value);

    void addShort(int value);

    void addInt(int value);

    void addLong(long value);

    void addString(String value);
}
