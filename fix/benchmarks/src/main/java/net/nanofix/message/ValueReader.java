package net.nanofix.message;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ValueReader {

    void onValue(ByteBuffer buffer, int valueOffset, int valueLen);

}
