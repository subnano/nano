package net.subnano.codec.json;

import io.nano.core.buffer.AsciiBufferUtil;
import net.subnano.codec.json.sample.SampleData;
import net.subnano.codec.json.sample.JacksonPriceCodec;
import net.subnano.codec.json.sample.MutablePrice;
import net.subnano.codec.json.sample.NanoPriceCodec;
import net.subnano.codec.json.sample.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class PriceParserTest {

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    @BeforeEach
    void setUp() {
        AsciiBufferUtil.putCharSequence(SampleData.SAMPLE_PRICE, buffer);
        buffer.flip();  // ready to be read
        buffer.mark();  // remember position
    }

    @Test
    void ensureBothParsersProduceSameResult() throws IOException {
        System.out.println(SampleData.SAMPLE_PRICE);
        JacksonPriceCodec jacksonCodec = new JacksonPriceCodec();
        Price priceByJackson = jacksonCodec.decode(buffer);
        System.out.println("Decoded: " + priceByJackson);

        MutablePrice priceByNano = new MutablePrice();
        NanoPriceCodec nanoCodec = new NanoPriceCodec();
        buffer.reset();
        nanoCodec.decode(buffer, priceByNano);
        System.out.println("Decoded: " + priceByNano);

        // ensure that they are equals
        assertPriceEquals(priceByJackson, priceByNano);
    }

    private void assertPriceEquals(Price p1, Price p2) {
        Assertions.assertThat(p1.getInstrument()).as("instrument").isEqualTo(p2.getInstrument());
        Assertions.assertThat(p1.getTimestamp()).as("timestamp").isEqualTo(p2.getTimestamp());
        Assertions.assertThat(p1.isSuccess()).as("success").isEqualTo(p2.isSuccess());
    }
}
