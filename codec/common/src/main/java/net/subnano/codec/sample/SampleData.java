package net.subnano.codec.sample;

/**
 * @author Mark Wardell
 */
public final class SampleData {

    public static final String SAMPLE_PRICE_JSON = "{\"event\": \"price\", \"success\": true, \"instrument\": \"ETHJPY.SPOT\", " +
            "\"buy\": [{\"quantity\": \"3.4274\", \"price\": \"25212\"}, " +
            "{\"quantity\": \"17.1371\", \"price\": \"25212\"}], " +
            "\"sell\": [{\"quantity\": \"3.4274\", \"price\": \"25169\"}, " +
            "{\"quantity\": \"17.1371\", \"price\": \"25167\"}], \"timestamp\": 1536217539975}";

    public static final MutablePrice SAMPLE_PRICE = newMutablePrice();

    // {"event":"price","success":true,"instrument":"BTCUSD.SPOT","levels":{"buy":[{"price":"6439.3","quantity":"0.1553"},{"price":"6439.3","quantity":"0.7764"}],"
    // sell":[{"price":"6423.2","quantity":"0.1553"},{"price":"6423.1","quantity":"0.7764"}]},"timestamp":1539683961266}
    private static MutablePrice newMutablePrice() {
        MutablePrice price = new MutablePrice();
        //price.instrument = "BTCUSD.SPOT";
        price.timeCreated = System.currentTimeMillis();
        price.bids[0].quantity = 15530000;  // 8dp
        price.bids[0].price = 64393000;     // 4dp
        price.bids[1].quantity = 77640000;  // 8dp
        price.bids[1].price = 64393000;     // 4dp
        price.asks[0].quantity = 15530000;  // 8dp
        price.asks[0].price = 64233200;     // 4dp
        price.asks[1].quantity = 77640000;  // 8dp
        price.asks[1].price = 64233100;     // 4dp
        return price;
    }
}
