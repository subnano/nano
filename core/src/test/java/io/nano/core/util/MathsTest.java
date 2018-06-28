package io.nano.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MathsTest {

    @Test
    void pow10() {
        for (int i = 0; i <= 9; i++)
            assertThat(Maths.pow10(i))
                    .as("%d pow10", i)
                    .isEqualTo((long) Math.pow(10, i));
    }

    @Test
    void pow() {
        for (int i = 0; i <= 18; i++)
            assertThat(Maths.pow(10, i))
                    .as("%d pow %d", 10, i)
                    .isEqualTo((long) Math.pow(10, i));
    }
}