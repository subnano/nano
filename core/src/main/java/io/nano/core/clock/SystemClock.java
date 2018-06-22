package io.nano.core.clock;

import java.util.concurrent.TimeUnit;

/**
 * Clock implementation using System.currentTimeMillis as a time source.
 *
 * Due to this limitation timing resolution is limited to milliseconds.
 *
 * @see NanoClock for an implementation with far more granular timings.
 */
public final class SystemClock implements Clock {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long currentTimeMicros() {
        return TimeUnit.MILLISECONDS.toMicros(currentTimeMillis());
    }

    @Override
    public long currentTimeNanos() {
        return TimeUnit.MILLISECONDS.toNanos(currentTimeMillis());
    }
}