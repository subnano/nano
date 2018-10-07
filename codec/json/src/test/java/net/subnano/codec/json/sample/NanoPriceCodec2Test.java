package net.subnano.codec.json.sample;

import io.nano.core.buffer.AsciiBufferUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static net.subnano.codec.json.sample.SampleData.SAMPLE_PRICE;

/**
 * @author Mark Wardell
 */
public class NanoPriceCodec2Test {

    @Test
    void name() {
        NanoPriceCodec2 codec = new NanoPriceCodec2();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        AsciiBufferUtil.putCharSequence(SAMPLE_PRICE, buffer);
        buffer.flip();  // ready to be read
        MutablePrice mutablePrice = new MutablePrice();

        codec.decode(buffer, mutablePrice);
    }
}