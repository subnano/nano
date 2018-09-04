package io.nano.core.time;

import io.nano.core.buffer.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * No Attempt is made to validate or convert the timezone. Assumed that the timezone is 'Z' for UTC.
 */
public class ISODateTimeDecoder {

    public static long decode(final String utcTimestamp) {
        return decode(ByteBufferUtil.wrap(utcTimestamp), 0);
    }

    public static long decode(final ByteBuffer buffer, final int offset) {
        return ISODateDecoder.decode(buffer, offset) + UtcTimeDecoder.decode(buffer, offset + 11);
    }
}
