package io.nano.core.clock;

import org.junit.jupiter.api.Test;

import java.util.function.LongSupplier;

import static org.assertj.core.api.Assertions.assertThat;

class SystemClockTest {

    @Test
    void currentTimeMillis() {
        Clock clock = new SystemClock();
        assertTimeSequencing(System::currentTimeMillis, clock::currentTimeMillis);
    }

    @Test
    void currentTimeMicros() {
        Clock clock = new SystemClock();
        assertThat(clock.currentTimeMicros() % 1_000).isZero();
        assertTimeSequencing(() -> System.currentTimeMillis() * 1_000, clock::currentTimeMicros);
    }

    @Test
    void currentTimeNanos() {
        Clock clock = new SystemClock();
        assertThat(clock.currentTimeMicros() % 1_000_000).isZero();
        assertTimeSequencing(() -> System.currentTimeMillis() * 1000000, clock::currentTimeNanos);
    }

    private static void assertTimeSequencing(LongSupplier knownSource, LongSupplier testedSource) {
        long timeBefore = knownSource.getAsLong();
        long timeToTest = testedSource.getAsLong();
        assertThat(timeToTest).isGreaterThanOrEqualTo(timeBefore);
        assertThat(knownSource.getAsLong()).isGreaterThanOrEqualTo(timeToTest);
    }
}