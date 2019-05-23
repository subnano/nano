package net.subnano.codec.wire;

import net.subnano.codec.sample.MutablePrice;
import net.subnano.codec.sample.Price;
import net.subnano.codec.sample.SampleData;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class PriceCodecTest {

    // JSON Price String (270 chars)
    // {"event":"price","success":true,"instrument":"BTCUSD.SPOT","levels":{"buy":[{"price":"6439.3","quantity":"0.1553"},{"price":"6439.3","quantity":"0.7764"}],"sell":[{"price":"6423.2","quantity":"0.1553"},{"price":"6423.1","quantity":"0.7764"}]},"timestamp":1539683961266}
    @Test
    void name() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        NanoEncoder encoder = new NanoEncoder(new ByteBufferEncoder(buffer));
        Price price = SampleData.SAMPLE_PRICE;
        PriceEncoder priceEncoder = new PriceEncoder(encoder);
        priceEncoder.encode(price);

        // Now we need to test we can decode
        BufferReader bufferReader = new ByteBufferReader(buffer);
        MutablePrice decodedPrice = new MutablePrice();
        PriceReader priceReader = new PriceReader(decodedPrice);
        NanoWireReader wireReader = new NanoWireReader(bufferReader);
        priceReader.decode(wireReader);

//        NanoDecoder wireDecoder = new NanoDecoder();
//        wireDecoder.parse(bufferDecoder, priceDecoder);

        PriceAssert.assertThat(decodedPrice).isEqualTo(price);
    }
}
