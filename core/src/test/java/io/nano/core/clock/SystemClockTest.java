package io.nano.core.clock;

import org.junit.jupiter.api.Test;

import java.util.function.LongSupplier;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemClockTest {

    private static final int THOUSAND = 1_000;
    private static final int MILLION = 1_000_000;

    private Clock clock = new SystemClock();

    @Test
    void currentTimeMillis() {
        assertTimeSequencing(System::currentTimeMillis, clock::currentTimeMillis);
    }

    @Test
    void currentTimeMicros() {
        assertThat(clock.currentTimeMicros() % THOUSAND).isZero();
        assertTimeSequencing(() -> System.currentTimeMillis() * THOUSAND, clock::currentTimeMicros);
    }

    @Test
    void currentTimeNanos() {
        assertThat(clock.currentTimeNanos() % MILLION).isZero();
        assertTimeSequencing(() -> System.currentTimeMillis() * MILLION, clock::currentTimeNanos);
    }

    private static void assertTimeSequencing(LongSupplier knownSource, LongSupplier testedSource) {
        long timeBefore = knownSource.getAsLong();
        long timeToTest = testedSource.getAsLong();
        assertThat(timeToTest).isGreaterThanOrEqualTo(timeBefore);
        assertThat(knownSource.getAsLong()).isGreaterThanOrEqualTo(timeToTest);
    }
}