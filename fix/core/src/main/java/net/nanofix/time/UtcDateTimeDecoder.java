package net.nanofix.time;

import io.nano.core.buffer.ByteBufferUtil;

import java.nio.ByteBuffer;

public class UtcDateTimeDecoder {

    public static long decode(final String utcTimestamp) {
        return decode(ByteBufferUtil.wrap(utcTimestamp), 0);
    }

    public static long decode(final ByteBuffer buffer, final int offset) {
        return UtcDateDecoder.decode(buffer, offset) +
                UtcTimeDecoder.decode(buffer, offset + 9);
    }
}
