package net.nanofix.message;

import java.nio.ByteBuffer;

public interface FIXMessageVisitor {

    /**
     * Callback as each tag value pair is iterated over in buffer.
     *
     * It can be assumed that valueIndex will be tagIndex + tagLen + 1
     */
    void onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen);

    void onError(int index, String message);

    /**
     * Controls buffer iteration logic so that the visitor decides when enough data has been received.
     */
    boolean complete();
}
