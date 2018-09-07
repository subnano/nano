package net.nanofix.message;

import io.nano.core.lang.ByteString;

public interface MessageAssembler {

    void addBooleanField(int tag, boolean value);

    void addByteField(int tag, byte value);

    void addIntField(int tag, int value);

    void addLongField(int tag, long value);

    void addStringField(int tag, ByteString value);

    void addTimestamp(int tag, long timestamp);

    void addBytesField(int tag, byte[] bytes);
}