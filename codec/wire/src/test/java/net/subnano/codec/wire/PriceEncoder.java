package net.subnano.codec.wire;

import net.subnano.codec.sample.Level;
import net.subnano.codec.sample.Price;

public class PriceEncoder {

    // TODO How to coordinate StreamIds
    private static final byte PRICE_STREAM_ID = 11;

    private final NanoEncoder encoder;

    // TODO Should be WireEncoder
    public PriceEncoder(NanoEncoder encoder) {
        this.encoder = encoder;
    }

    public void encode(Price price) {
        encoder.streamId(PRICE_STREAM_ID);
        encoder.addString(price.getInstrument());
        encoder.addLong(price.getTimeCreated());
        addLevels(encoder, price.getBids());
        addLevels(encoder, price.getAsks());
    }

    private void addLevels(NanoEncoder encoder, Level[] levels) {
        encoder.addLongArray(levels.length, b -> {
            for (Level level : levels) {
                b.addLong(level.getQuantity());
            }
        });
        encoder.addLongArray(levels.length, b -> {
            for (Level level : levels) {
                b.addLong(level.getPrice());
            }
        });
    }
}
