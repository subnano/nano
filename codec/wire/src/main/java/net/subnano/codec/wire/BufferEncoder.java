package net.subnano.codec.wire;

public interface BufferEncoder {

    void addByte(byte value);

    void addShort(short value);

    void addInt(int value);

    void addLong(long value);

    void addDouble(double value);

    void addString(String value);
}
