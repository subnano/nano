package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

/**
 * Creates a string representation of FIX message from decoding a ByteBuffer.
 * Not to be used in critical sections of code as allocation is poor.
 */
public class MessageStringBuilder implements FIXMessageVisitor {

    private final StringBuilder sb;
    private final char delimiter;

    public MessageStringBuilder() {
        this.sb = new StringBuilder();
        this.delimiter = (char)FIXBytes.PIPE;
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        int valueIndex = tagIndex + tagLen + 1;
        sb.append(new String(ByteBufferUtil.asByteArray(buffer, tagIndex, tagLen))).append('=').append(new String(
                ByteBufferUtil.asByteArray(buffer, valueIndex, valueLen))).append(delimiter);
        return true;
    }

    @Override
    public void onError(int index, String message) {
        throw new IllegalArgumentException(String.format("Index %d - %s", index, message));
    }

    @Override
    public boolean complete() {
        return false;
    }

    public String asString() {
        return sb.toString();
    }
}
