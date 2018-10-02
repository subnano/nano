package net.nanofix.message;

import java.nio.ByteBuffer;

public interface FIXMessageVisitor2 {

    /**
     * Callback as each tag value pair is iterated over in buffer.
     */
    boolean onTag(ByteBuffer buffer, int tag, int valueOffset, int valueLen);

    void onError(int offset, String message);

    /**
     * Controls buffer iteration logic so that the visitor decides when enough data has been received.
     */
    boolean complete();
}
