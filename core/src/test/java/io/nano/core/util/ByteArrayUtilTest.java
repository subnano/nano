package io.nano.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: Mark
 * Date: 24/03/12
 * Time: 14:53
 */
public class ByteArrayUtilTest {

    @Test
    void testIsDigit() {
        assertThat(ByteArrayUtil.isDigit((byte) '0')).isTrue();
        assertThat(ByteArrayUtil.isDigit((byte) '9')).isTrue();
        assertThat(ByteArrayUtil.isDigit((byte) 'A')).isFalse();
    }

    @Test
    void testToInteger() {
        assertThat(ByteArrayUtil.toInteger(new byte[]{49, 50})).isEqualTo(12);
        assertThat(ByteArrayUtil.toInteger(new byte[]{49, 50, 51})).isEqualTo(123);
        assertThat(ByteArrayUtil.toInteger(new byte[]{49, 50, 51, 52})).isEqualTo(1234);
        assertThat(ByteArrayUtil.toInteger(new byte[]{45, 49, 50, 51, 52})).isEqualTo(-1234);
    }

}
