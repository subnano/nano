package io.nano.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void formatNumber() {
        assertFormat(123.12345D, 3, "123.123");
        assertFormat(0.1111111D, 4, "0.1111");
        assertFormat(123.4440000D, 4, "123.4440");
        assertFormat(123.4440000D, 2, "123.44");
        assertFormat(0.1, 3, "0.100");
        assertFormat(456D, 0, "456");
    }

    @Test
    void formatANotoriouslyDifficultNumber() {
        assertFormat((29.0 * 0.01) * 100, 1, "29.0");
    }

    private void assertFormat(double value, int precision, String expected) {
        String actual = StringUtil.formatNumber(value, precision);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual).isEqualTo(String.format("%." + precision + "f", value));
    }
}