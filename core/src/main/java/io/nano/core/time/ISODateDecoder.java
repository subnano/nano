package io.nano.core.time;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.time.TimeUtil;

import java.nio.ByteBuffer;

public class ISODateDecoder {

    public static long decode(final ByteBuffer buffer, final int offset) {
        final int year = AsciiBufferUtil.getInt(buffer, offset, 4);
        final int month = AsciiBufferUtil.getInt(buffer, offset + 5, 2);
        final int day = AsciiBufferUtil.getInt(buffer, offset + 8, 2);
        long epochDay = TimeUtil.toEpochDay(year, month, day);
        return epochDay * TimeUtil.MILLIS_PER_DAY;
    }
}
