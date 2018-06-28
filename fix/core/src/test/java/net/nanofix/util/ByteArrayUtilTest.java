package net.nanofix.util;

import io.nano.core.clock.SystemClock;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: Mark
 * Date: 24/03/12
 * Time: 14:53
 */
public class ByteArrayUtilTest {

    SystemClock clock = new SystemClock();

    @Test
    public void testIsDigit() throws Exception {
        assertThat(ByteArrayUtil.isDigit((byte) '0')).isTrue();
        assertThat(ByteArrayUtil.isDigit((byte) '9')).isTrue();
        assertThat(ByteArrayUtil.isDigit((byte) 'A')).isFalse();
    }

    @Test
    public void testToInteger() throws Exception {
        assertThat(ByteArrayUtil.toInteger(new byte[]{49, 50})).isEqualTo(12);
        assertThat(ByteArrayUtil.toInteger(new byte[]{49, 50, 51})).isEqualTo(123);
        assertThat(ByteArrayUtil.toInteger(new byte[]{49, 50, 51, 52})).isEqualTo(1234);
        assertThat(ByteArrayUtil.toInteger(new byte[]{45, 49, 50, 51, 52})).isEqualTo(-1234);
    }

    @Test
    public void testBenchmark() {
        final int TEST_LOOPS = 1000 * 1000 * 1000;

        long start = clock.currentTimeMillis();
        for (int i = 0; i <= TEST_LOOPS; i++) {
            long result = 0 - 12345L;
        }
        TestHelper.printResults("subtract", TEST_LOOPS, clock.currentTimeMillis() - start);

        start = clock.currentTimeMillis();
        for (int i = 0; i <= TEST_LOOPS; i++) {
            long result = 12345L * -1;
        }
        TestHelper.printResults("multiply", TEST_LOOPS, clock.currentTimeMillis() - start);
    }
}
