package io.nano.core.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
class AsciiBufferUtilTest {

    private ByteBuffer buffer = ByteBuffer.allocate(32);

    @Test
    void putStringAbsolute() {
        String value = "Hello world!";
        int len = AsciiBufferUtil.putCharSequence(value, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len)).isEqualTo(value);
        assertThat(buffer.position()).isZero();
    }

    @Test
    void putStringRelative() {
        String value = "Hello world!";
        int len = AsciiBufferUtil.putCharSequence(value, buffer);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len)).isEqualTo(value);
        assertThat(buffer.position()).isEqualTo(12);
    }

    @Test
    void putIntAbsolute() {
        AsciiBufferUtil.putInt(1234, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, 4)) .isEqualTo("1234");
        assertThat(buffer.position()).isZero();
    }

    @Test
    void putIntRelative() {
        AsciiBufferUtil.putInt(1234, buffer);
        assertThat(AsciiBufferUtil.getString(buffer, 0, 4)) .isEqualTo("1234");
        assertThat(buffer.position()).isEqualTo(4);
    }

    @Test
    void putInt() {
        assertIntBuffer(0);
        assertIntBuffer(3);
        assertIntBuffer(37);
        assertIntBuffer(321);
        assertIntBuffer(4321);
        assertIntBuffer(54321);
        assertIntBuffer(654321);
        assertIntBuffer(7654321);
        assertIntBuffer(87654321);
        assertIntBuffer(987654321);
        assertIntBuffer(Integer.MAX_VALUE);
    }

    @Test
    void putIntPadded() {
        assertIntBufferPadded(0, 2, "00");
        assertIntBufferPadded(3, 4, "0003");
        assertIntBufferPadded(901, 3, "901");
        assertIntBufferPadded(5, 1, "5");
        assertIntBufferPadded(1234567890, 10, "1234567890");
        assertIntBufferPadded(Integer.MAX_VALUE, 10, String.valueOf(Integer.MAX_VALUE));
    }

    @Test
    void putLong() {
        assertLongBuffer(0);
        assertLongBuffer(3);
        assertLongBuffer(901);
        assertLongBuffer(987654321);
        assertLongBuffer(4536039866140L);
        assertLongBuffer(Long.MAX_VALUE);
    }

    @Test
    void putLongAbsolute() {
        AsciiBufferUtil.putLong(123456L, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, 6)) .isEqualTo("123456");
        assertThat(buffer.position()).isZero();
    }

    @Test
    void putLongRelative() {
        AsciiBufferUtil.putLong(123456L, buffer);
        assertThat(AsciiBufferUtil.getString(buffer, 0, 6)) .isEqualTo("123456");
        assertThat(buffer.position()).isEqualTo(6);
    }

    @Test
    void getInt() {
        AsciiBufferUtil.putCharSequence("001742", buffer);
        assertThat(AsciiBufferUtil.getInt(buffer, 0, 4)).isEqualTo(17);
        assertThat(AsciiBufferUtil.getInt(buffer, 4, 2)).isEqualTo(42);
    }

    private void assertIntBuffer(int value) {
        String expected = String.valueOf(value);

        // first we try absolute offset
        buffer.clear();
        int len = AsciiBufferUtil.putInt(value, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len))
                .as("putInt(%d)", value)
                .isEqualTo(expected);

        // next we try relative offset
        buffer.clear();
        len = AsciiBufferUtil.putInt(value, buffer);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len))
                .as("putInt(%d)", value)
                .isEqualTo(expected);
    }

    private void assertLongBuffer(long value) {
        String expected = String.valueOf(value);

        // first we try absolute offset
        buffer.clear();
        int len = AsciiBufferUtil.putLong(value, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len))
                .as("putLong(%d)", value)
                .isEqualTo(expected);

        // next we try relative offset
        buffer.clear();
        len = AsciiBufferUtil.putLong(value, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len))
                .as("putLong(%d)", value)
                .isEqualTo(expected);
    }

    private void assertIntBufferPadded(int value, int len, String expected) {
        buffer.clear();
        AsciiBufferUtil.putInt(value, len, buffer, 0);
        assertThat(AsciiBufferUtil.getString(buffer, 0, len)).isEqualTo(expected);
    }

}