package net.nanofix.message;

import net.nanofix.time.UtcDateTimeEncoder;
import io.nano.core.util.ByteArrayUtil;
import net.nanofix.util.ByteString;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

/**
 * Implements FixBuffer with an underlying ByteBuffer implementation.
 **/
public class FixBuffer implements MessageAssembler {

    private final UtcDateTimeEncoder timeEncoder = new UtcDateTimeEncoder();

    protected ByteBuffer buffer;

    public FixBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public void wrap(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void addBooleanField(int tag, boolean value) {
        addByteField(tag, value ? FIXBytes.FIX_TRUE : FIXBytes.FIX_FALSE);
    }

    @Override
    public void addByteField(int tag, byte value) {
        buffer.put(ByteArrayUtil.asByteArray(tag));
        buffer.put(FIXBytes.EQUALS);
        buffer.put(value);
        buffer.put(FIXBytes.SOH);
    }

    @Override
    public void addIntField(int tag, int value) {
        addBytesField(tag, ByteArrayUtil.asByteArray(value));
    }

    @Override
    public void addLongField(int tag, long value) {
        addBytesField(tag, ByteArrayUtil.asByteArray(value));
    }

    @Override
    public void addStringField(int tag, ByteString value) {
        addBytesField(tag, value.bytes());
    }

    @Override
    public void addTimestamp(int tag, long timestamp) {
        buffer.put(ByteArrayUtil.asByteArray(tag));
        buffer.put(FIXBytes.EQUALS);
        int pos = buffer.position();
        pos += timeEncoder.encode(timestamp, buffer, pos);
        buffer.position(pos);
        buffer.put(FIXBytes.SOH);
    }

    @Override
    public void addBytesField(int tag, byte[] bytes) {
        addBytesWithDelimiters(buffer, ByteArrayUtil.asByteArray(tag), bytes);
    }

    protected void addBytesWithDelimiters(ByteBuffer buffer, byte[] tagAsBytes, byte[] bytes) {
        buffer.put(tagAsBytes);
        buffer.put(FIXBytes.EQUALS);
        buffer.put(bytes);
        buffer.put(FIXBytes.SOH);
    }

    protected ByteBuffer buffer() {
        return buffer;
    }

}
