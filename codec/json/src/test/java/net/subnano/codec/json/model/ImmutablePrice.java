package net.subnano.codec.json.model;

import java.util.Arrays;

/**
 * @author Mark Wardell
 */
public class ImmutablePrice implements Price {

    private final String instrument;
    private final long timestamp;
    private final boolean success;
    private final Level[] bids;
    private final Level[] asks;

    public ImmutablePrice(String instrument, long timestamp, boolean success, Level[] bids, Level[] asks) {
        this.instrument = instrument;
        this.timestamp = timestamp;
        this.success = success;
        this.bids = bids;
        this.asks = asks;
    }

    @Override
    public String getInstrument() {
        return instrument;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Level[] getBids() {
        return bids;
    }

    @Override
    public Level[] getAsks() {
        return asks;
    }

    @Override
    public String toString() {
        return "ImmutablePrice{" +
                "instrument='" + instrument + '\'' +
                ", timestamp=" + timestamp +
                ", success=" + success +
                ", bids=" + Arrays.toString(bids) +
                ", asks=" + Arrays.toString(asks) +
                '}';
    }

    public static class ImmutableLevel implements Level {
        final long quantity;
        final long price;

        public ImmutableLevel(long quantity, long price) {
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public long getQuantity() {
            return quantity;
        }

        @Override
        public long getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "[" + quantity + ',' + price + ']';
        }
    }
}
