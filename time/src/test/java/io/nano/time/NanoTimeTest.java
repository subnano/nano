package io.nano.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Run with -XX:+TieredCompilation -XX:+PrintCompilation to see the compiler kick in
 */
public class NanoTimeTest {

    private static final int RUN_COUNT = 200_000;

    @Test
    void currentTimeMicros() {
        long lastTime = 0;
        for (int i = 0; i < RUN_COUNT; i++) {
            long currentTimeMicros = NanoTime.currentTimeMicros();
            assertThat(currentTimeMicros).isGreaterThanOrEqualTo(lastTime);
            lastTime = currentTimeMicros;
        }
    }

    @Test
    void currentTimeNanos() {
        long lastTime = 0;
        for (int i = 0; i < RUN_COUNT; i++) {
            long currentTimeNanos = NanoTime.currentTimeNanos();
            assertThat(currentTimeNanos).isGreaterThanOrEqualTo(lastTime);
            lastTime = currentTimeNanos;
        }
    }

    @Test
    void microsToInstant() {
        Instant instantBefore = Instant.now();
        long millis = MICROSECONDS.toMillis(NanoTime.currentTimeMicros());
        Instant instantFromMicros = Instant.ofEpochMilli(millis);
        assertThat(instantFromMicros).isAfterOrEqualTo(instantBefore);
        assertThat(Instant.now()).isAfterOrEqualTo(instantFromMicros);
    }

    @Test
    void nanosToInstant() {
        Instant instantBefore = Instant.now();
        long millis = NANOSECONDS.toMillis(NanoTime.currentTimeNanos());
        Instant instantFromNanos = Instant.ofEpochMilli(millis);
        assertThat(instantFromNanos).isAfterOrEqualTo(instantBefore);
        assertThat(Instant.now()).isAfterOrEqualTo(instantFromNanos);
    }
}