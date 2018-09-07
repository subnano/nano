package io.nano.core.lang;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
class ByteStringTest {

    @Test
    void equalityOfNullStrings() {
        assertThat(ByteString.of((String)null)).isEqualTo(ByteString.of((String)null));
    }

    @Test
    void equalityOfEmptyStrings() {
        assertThat(ByteString.of("")).isEqualTo(ByteString.of(""));
    }

    @Test
    void equalityOfRegularStrings() {
        assertThat(ByteString.of("abc")).isEqualTo(ByteString.of("abc"));
    }

    @Test
    void equalityOfStringsWithSameLength() {
        assertThat(ByteString.of("abc")).isNotEqualTo(ByteString.of("acb"));
    }

}