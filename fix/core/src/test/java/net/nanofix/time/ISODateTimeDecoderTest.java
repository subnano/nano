package net.nanofix.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

class ISODateTimeDecoderTest {

    private ByteBuffer buffer;
    private DateFormat formatter;

    @BeforeEach
    void setUp() {
        buffer = ByteBuffer.allocate(32);
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void decodeIsoTimestamp() throws Exception {
        assertDecode("2018-08-06T10:49:02.971Z");
    }

    private void assertDecode(String expected) throws Exception {
        long expectedMillis = Date.from(Instant.parse(expected)).getTime();
        buffer.clear();
        buffer.put(expected.getBytes(StandardCharsets.US_ASCII), 0, expected.length());
        long epochMillis = ISODateTimeDecoder.decode(buffer, 0);
        Assertions.assertThat(epochMillis).isEqualTo(expectedMillis);
    }

}