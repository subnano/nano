package io.nano.core.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class ByteBufferUtilTest {

    private ByteBuffer buffer = ByteBuffer.allocate(16);


    @Test
    void putNumber() {
        assertBuffer(0, 2, "00");
        assertBuffer(3, 4, "0003");
        assertBuffer(901, 3, "901");
        assertBuffer(5, 1, "5");
        assertBuffer(1234567890, 10, "1234567890");
    }

    @Test
    void hexDump() {
        //String random = "This is what you want to see in 25/12/2512";
        String random = "Hello 25/12";
        ByteBuffer buffer = ByteBuffer.allocate(15);
        buffer.put(random.getBytes(StandardCharsets.US_ASCII));
        assertThat(ByteBufferUtil.hexDump(buffer))
                .isEqualTo("48 65 6C 6C 6F 20 32 35 2F 31 32 00 00 00 00   Hello 25/12....");
    }

    private void assertBuffer(int value, int len, String expected) {
        buffer.clear();
        ByteBufferUtil.putNumber(value, len, buffer, 0);
        String actual = new String(buffer.array(), 0, len, StandardCharsets.US_ASCII);
        assertThat(actual).isEqualTo(expected);
    }
}