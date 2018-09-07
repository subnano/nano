package net.subnano.codec.json;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.lang.ByteString;
import net.subnano.codec.json.model.MutablePrice;
import org.decimal4j.api.DecimalArithmetic;
import org.decimal4j.immutable.Decimal4f;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class NanoPriceCodec {

    private static final DecimalArithmetic ARITHMETIC = Decimal4f.DEFAULT_ARITHMETIC;

    private final JsonByteParser parser = new JsonByteParser();
    private final JsonPriceVisitor visitor = new JsonPriceVisitor();

    public void decode(ByteBuffer buffer, MutablePrice price) {
        visitor.withPrice(price);
        price.clear();
        parser.parse(buffer, visitor);
    }

    private class JsonPriceVisitor implements JsonVisitor {

        private MutablePrice price;
        private ByteString name;
        private MutablePrice.MutableLevel[] side;
        private int depth = 0;

        public void withPrice(MutablePrice price) {
            this.price = price;
        }

        @Override
        public void onString(ByteBuffer buffer, int offset, int len) {

            // if name is null then we record the attribute
            if (name == null) {
                name = getAttributeName(buffer, offset, len);
            }

            // if name is set then we have an attribute value
            else {
                if (PriceBytes.EVENT.equals(name)) {
                    // ignore the confirmation of 'event' == 'price'
                    // This needs to be determined to obtain correct JSON codec in advance
                    name = null;
                }
                else if (PriceBytes.INSTRUMENT.equals(name)) {
                    // TODO do NOT allocate - lookup from a cache
                    //instrumentCache.getOrCreate();
                    price.instrument = ByteBufferUtil.asByteString(buffer, offset, len);
                    name = null;
                }
                else if (PriceBytes.QUANTITY.equals(name)) {
                    long qty = ARITHMETIC.parse(AsciiBufferUtil.getString(buffer, offset, len));
                    price.setQuantity(depth, side, qty);
                    name = null;
                }
                else if (PriceBytes.PRICE.equals(name)) {
                    long px = ARITHMETIC.parse(AsciiBufferUtil.getString(buffer, offset, len));
                    price.setPrice(depth++, side, px);
                    name = null;
                }
                else {
                    throw new IllegalArgumentException("Unexpected string @ " + offset + " '"
                            + AsciiBufferUtil.getString(buffer, offset, len) + "'");
                }
            }
        }

        @Override
        public void onNumber(ByteBuffer buffer, int offset, int len) {
            if (name == null) {
                throw new IllegalArgumentException("Encountered a number when attribute not set");
            }
            else if (PriceBytes.TIMESTAMP.equals(name)) {
                price.timestamp = AsciiBufferUtil.getLong(buffer, offset, len);
                name = null;
            }
            else {
                throw new IllegalArgumentException("Unexpected number");
            }
        }

        @Override
        public void onError(ByteBuffer buffer, int offset, String cause) {

        }

        @Override
        public void onBoolean(ByteBuffer buffer, int offset, boolean value) {
            if (name == null) {
                throw new IllegalArgumentException("Encountered a number when attribute not set");
            }
            if (PriceBytes.SUCCESS.equals(name)) {
                price.success = value;
                name = null;
            }
            else {
                throw new IllegalArgumentException("Unexpected boolean");
            }
        }

        @Override
        public void startObject(ByteBuffer buffer, int offset) {

        }

        @Override
        public void endObject(ByteBuffer buffer, int offset) {

        }

        @Override
        public void startArray(ByteBuffer buffer, int offset) {
            depth = 0;
            if (PriceBytes.BUY.equals(name)) {
                side = price.bids;
                name = null;
            }
            else if (PriceBytes.SELL.equals(name)) {
                side = price.asks;
                name = null;
            }
        }

        @Override
        public void endArray(ByteBuffer buffer, int offset) {

        }

        @Override
        public void comma(ByteBuffer buffer, int offset) {

        }

        @Override
        public void colon(ByteBuffer buffer, int offset) {

        }

        private ByteString getAttributeName(ByteBuffer buffer, int offset, int len) {
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.EVENT)) {
                return PriceBytes.EVENT;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.TIMESTAMP)) {
                return PriceBytes.TIMESTAMP;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.SUCCESS)) {
                return PriceBytes.SUCCESS;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.INSTRUMENT)) {
                return PriceBytes.INSTRUMENT;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.BUY)) {
                return PriceBytes.BUY;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.SELL)) {
                return PriceBytes.SELL;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.QUANTITY)) {
                return PriceBytes.QUANTITY;
            }
            if (ByteBufferUtil.equals(buffer, offset, len, PriceBytes.PRICE)) {
                return PriceBytes.PRICE;
            }
            throw new IllegalArgumentException("Unsupported attribute or JSON schema invalid");
        }
    }

    static class PriceBytes {
        static ByteString EVENT = ByteString.of("event");
        static ByteString TIMESTAMP = ByteString.of("timestamp");
        static ByteString SUCCESS = ByteString.of("success");
        static ByteString INSTRUMENT = ByteString.of("instrument");
        static ByteString BUY = ByteString.of("buy");
        static ByteString SELL = ByteString.of("sell");
        static ByteString QUANTITY = ByteString.of("quantity");
        static ByteString PRICE = ByteString.of("price");   // can also be an attribute value
    }

    /*
    startObject(0)
    onString(2,5) "event"
    colon(8)
    onString(11,5) "price"
    comma(17)
    onString(20,7) "success"
    colon(28)
    onBoolean(30,true)
    comma(34)
    onString(37,10) "instrument"
    colon(48)
    onString(51,11) "ETHJPY.SPOT"
    comma(63)
    onString(66,3) "buy"
    colon(70)
    startArray(72)
    startObject(73)
    onString(75,8) "quantity"
    colon(84)
    onString(87,6) "3.4274"
    comma(94)
    onString(97,5) "price"
    colon(103)
    onString(106,5) "25212"
    endObject(112)
    comma(113)
    startObject(115)
    onString(117,8) "quantity"
    colon(126)
    onString(129,7) "17.1371"
    comma(137)
    onString(140,5) "price"
    colon(146)
    onString(149,5) "25212"
    endObject(155)
    endArray(156)
    comma(157)
    onString(160,4) "sell"
    colon(165)
    startArray(167)
    startObject(168)
    onString(170,8) "quantity"
    colon(179)
    onString(182,6) "3.4274"
    comma(189)
    onString(192,5) "price"
    colon(198)
    onString(201,5) "25169"
    endObject(207)
    comma(208)
    startObject(210)
    onString(212,8) "quantity"
    colon(221)
    onString(224,7) "17.1371"
    comma(232)
    onString(235,5) "price"
    colon(241)
    onString(244,5) "25167"
    endObject(250)
    endArray(251)
    comma(252)
    onString(255,9) "timestamp"
    colon(265)
    onNumber(267,13) "1536217539975"
    endObject(280)
     */
}
