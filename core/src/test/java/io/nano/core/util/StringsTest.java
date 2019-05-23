package io.nano.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringsTest {

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

    @Test
    void repeat() {
        assertThat(Strings.repeat(null, 17)).isNull();
        assertThat(Strings.repeat("abc", 0)).isEqualTo("");
        assertThat(Strings.repeat("x", 4)).isEqualTo("xxxx");
        assertThat(Strings.repeat("abc", 3)).isEqualTo("abcabcabc");
    }

    @Test
    void combineTwoStrings() {
        assertThat(Strings.combine(null, "only")).isEqualTo("only");
        assertThat(Strings.combine("only", null)).isEqualTo("only");
        assertThat(Strings.combine("one", "two")).isEqualTo("onetwo");
    }

    @Test
    void combineThreeStrings() {
        assertThat(Strings.combine("one", "two", "three")).isEqualTo("onetwothree");
        assertThat(Strings.combine(null, "one", "two")).isEqualTo("onetwo");
        assertThat(Strings.combine("one", null, "two")).isEqualTo("onetwo");
        assertThat(Strings.combine("one", "two", null)).isEqualTo("onetwo");
    }

    private void assertFormat(double value, int precision, String expected) {
        String actual = Strings.formatNumber(value, precision);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual).isEqualTo(String.format("%." + precision + "f", value));
    }
}