package io.nano.core.time;

import io.nano.core.buffer.AsciiBufferUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * TODO Add support for with/without millis
 * TODO Add support for micros (nanos?)
 */
public class UtcTimeDecoder {

    public static long decode(final ByteBuffer buffer, final int offset) {
        final int hour = AsciiBufferUtil.getInt(buffer, offset, 2);
        final int minute = AsciiBufferUtil.getInt(buffer, offset + 3, 2);
        final int second = AsciiBufferUtil.getInt(buffer, offset + 6, 2);
        final int millisecond = AsciiBufferUtil.getInt(buffer, offset + 9, 3);
        // TODO validate numeric range
        return TimeUnit.HOURS.toMillis(hour)
                + TimeUnit.MINUTES.toMillis(minute)
                + TimeUnit.SECONDS.toMillis(second)
                + millisecond;
    }
}
