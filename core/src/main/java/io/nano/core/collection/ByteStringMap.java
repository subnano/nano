package io.nano.core.collection;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.lang.ByteString;

import java.nio.ByteBuffer;

/**
 * A collection of ByteString instances to avoid object allocation.
 *
 * <p>This implementation uses an array to the ByteString instances so only suited for very small collections
 * due to a complexity O(n).</p>
 *
 * TODO not even a map so should be renamed.
 *
 * @author Mark Wardell
 */
public class ByteStringMap {

    private final ByteString[] values;

    private int length;

    public ByteStringMap(int capacity) {
        this.values = new ByteString[capacity];
        this.length = 0;
    }

    public ByteString getOrCreate(ByteBuffer buffer, int offset, int len) {
        ByteString existing = lookup(buffer, offset, len);
        if (existing == null) {
            existing = ByteBufferUtil.asByteString(buffer, offset, len);
            values[length++] = existing;
        }
        return existing;
    }

    private ByteString lookup(ByteBuffer buffer, int offset, int len) {
        for (int i = 0; i < length; i++) {
            if (ByteBufferUtil.equals(buffer, offset, len, values[i])) {
                return values[i];
            }
        }
        return null;
    }

}
