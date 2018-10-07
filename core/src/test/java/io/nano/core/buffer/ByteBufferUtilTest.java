package io.nano.core.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ByteBufferUtilTest {

    @Test
    void hexDump() {
        //String random = "This is what you want to see in 25/12/2512";
        String random = "Hello 25/12";
        ByteBuffer buffer = ByteBuffer.allocate(15);
        buffer.put(random.getBytes(StandardCharsets.US_ASCII));
        assertThat(ByteBufferUtil.hexDump(buffer))
                .isEqualTo("48 65 6C 6C 6F 20 32 35 2F 31 32 00 00 00 00   Hello 25/12....");
    }

}