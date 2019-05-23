package net.subnano.codec.wire;

import net.subnano.codec.sample.MutablePrice;

public class PriceReader {

    private final MutablePrice price;

    public PriceReader(MutablePrice price) {
        this.price = price;
    }

    public void decode(NanoWireReader reader) {
        byte streamId = reader.streamId();
        // TODO check that streamId matches our expectation
        price.instrument = reader.readString();
        price.instrument = reader.readString();
        price.timeCreated = reader.readLong();
    }
}
