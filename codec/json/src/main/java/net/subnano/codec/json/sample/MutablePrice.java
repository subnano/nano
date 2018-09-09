package net.subnano.codec.json.sample;

import io.nano.core.lang.ByteString;

import java.util.Arrays;

/**
 * @author Mark Wardell
 */
public class MutablePrice implements Price {

    public ByteString instrument;
    public long timestamp;
    public boolean success;
    public MutableLevel[] bids = new MutableLevel[2];
    public MutableLevel[] asks = new MutableLevel[2];

    public MutablePrice() {
        for (int i=0; i<bids.length; i++) {
            bids[i] = new MutableLevel();
            asks[i] = new MutableLevel();
        }
    }

    public void clear() {
        instrument = null;
        timestamp = 0;
        success = false;
        for (int i=0; i<bids.length; i++) {
            bids[i].quantity = Long.MIN_VALUE;
            bids[i].price = Long.MIN_VALUE;
            asks[i].quantity = Long.MIN_VALUE;
            asks[i].price = Long.MIN_VALUE;
        }
    }

    @Override
    public String getInstrument() {
        return instrument.toString();
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
        return "MutablePrice{" +
                "instrument='" + instrument + '\'' +
                ", timestamp=" + timestamp +
                ", success=" + success +
                ", bids=" + Arrays.toString(bids) +
                ", asks=" + Arrays.toString(asks) +
                '}';
    }

    public void setQuantity(int depth, MutableLevel[] side, long quantity) {
        side[depth].quantity = quantity;
    }

    public void setPrice(int depth, MutableLevel[] side, long price) {
        side[depth].price = price;
    }

    public static class MutableLevel implements Level {

        long quantity;
        long price;

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
