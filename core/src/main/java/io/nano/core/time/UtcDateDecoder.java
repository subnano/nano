package io.nano.core.time;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.time.TimeUtil;

import java.nio.ByteBuffer;

public class UtcDateDecoder {

    public static long decode(final ByteBuffer buffer, final int offset) {
        final int year = AsciiBufferUtil.getInt(buffer, offset, 4);
        final int month = AsciiBufferUtil.getInt(buffer, offset + 4, 2);
        final int day = AsciiBufferUtil.getInt(buffer, offset + 6, 2);
        // TODO validate numeric range
        long epochDay = TimeUtil.toEpochDay(year, month, day);
        return epochDay * TimeUtil.MILLIS_PER_DAY;
    }
}
