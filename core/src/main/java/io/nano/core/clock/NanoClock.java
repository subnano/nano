package io.nano.core.clock;

import io.nano.time.NanoTime;

public final class NanoClock implements Clock {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long currentTimeMicros() {
        return NanoTime.currentTimeMicros();
    }

    @Override
    public long currentTimeNanos() {
        return NanoTime.currentTimeNanos();
    }
}
