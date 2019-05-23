package net.subnano.codec.wire;

public interface BufferReader {

    int position();

    byte readByte(int offset);

    byte readByte();

    short readShort(int offset);

    int readInt(int offset);

    long readLong(int offset);

    long readLong();

    double readDouble(int offset);

    String readString(int offset, int len);
}
