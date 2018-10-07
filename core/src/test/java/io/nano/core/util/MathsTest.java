package io.nano.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MathsTest {

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

    @Test
    void numberDigits() {
        assertNumberDigits(3,1);
        assertNumberDigits(19,2);
        assertNumberDigits(177,3);
        assertNumberDigits(2192,4);
        assertNumberDigits(26312,5);
        assertNumberDigits(309173,6);
        assertNumberDigits(3718262,7);
        assertNumberDigits(41826373,8);
        assertNumberDigits(437172534,9);
        assertNumberDigits(2091625267,10);
    }

    @Test
    void numberDigitsLong() {
        assertNumberDigits(3L,1);
        assertNumberDigits(19L,2);
        assertNumberDigits(177L,3);
        assertNumberDigits(2192L,4);
        assertNumberDigits(26312L,5);
        assertNumberDigits(309173L,6);
        assertNumberDigits(3718262L,7);
        assertNumberDigits(41826373L,8);
        assertNumberDigits(437172534L,9);
        assertNumberDigits(5091625267L,10);
        assertNumberDigits(59192817102L,11);
        assertNumberDigits(617123782392L,12);
        assertNumberDigits(6817163567541L,13);
    }

    private void assertNumberDigits(int n, int expected) {
        assertThat(Maths.numberDigits(n)).as("Maths.numberDigits(%d)", n).isEqualTo(expected);
        assertThat((int)Math.log10(n) + 1).as("Math.log10(%d)", n).isEqualTo(expected);
    }

    private void assertNumberDigits(long n, int expected) {
        assertThat(Maths.numberDigits(n)).as("Maths.numberDigits(%d)", n).isEqualTo(expected);
        assertThat((int)Math.log10(n) + 1).as("Math.log10(%d)", n).isEqualTo(expected);
    }
}