package net.subnano.codec.wire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class WireTypeTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void name() {
        assertThat(WireType.name(WireType.BYTE)).isEqualTo("Byte");
        assertThat(WireType.name(WireType.SHORT)).isEqualTo("Short");
        assertThat(WireType.name(WireType.INT)).isEqualTo("Int");
        assertThat(WireType.name(WireType.LONG)).isEqualTo("Long");
        assertThat(WireType.name(WireType.DOUBLE)).isEqualTo("Double");
        assertThat(WireType.name(WireType.STRING)).isEqualTo("String");
        assertThat(WireType.name(WireType.TEXT)).isEqualTo("Text");
    }

    @Test
    void nameOfInvalidType() {
        Throwable thrown = catchThrowable(() -> WireType.name(77));
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Invalid wire type");
    }

    @Test
    void values() {
        assertThat(WireType.values()).isEqualTo(new byte[]{
                WireType.BYTE,
                WireType.SHORT,
                WireType.INT,
                WireType.LONG,
                WireType.DOUBLE,
                WireType.STRING,
                WireType.TEXT
        });
    }

    @Test
    void sizeOf() {
        assertThat(WireType.sizeOf(WireType.BYTE)).isEqualTo(1);
        assertThat(WireType.sizeOf(WireType.SHORT)).isEqualTo(2);
        assertThat(WireType.sizeOf(WireType.INT)).isEqualTo(4);
        assertThat(WireType.sizeOf(WireType.LONG)).isEqualTo(8);
        assertThat(WireType.sizeOf(WireType.DOUBLE)).isEqualTo(8);
        assertThat(WireType.sizeOf(WireType.STRING)).isEqualTo(-1);
        assertThat(WireType.sizeOf(WireType.TEXT)).isEqualTo(-1);
    }

    @Test
    void sizeOfInvalidType() {
        Throwable thrown = catchThrowable(() -> WireType.sizeOf(99));
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Invalid wire type");
    }
}