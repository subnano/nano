package net.subnano.codec.json.sample;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.collection.ByteStringMap;
import io.nano.core.lang.ByteString;
import net.subnano.codec.json.JsonByteParser;
import net.subnano.codec.json.JsonVisitor;
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
    private final BufferCharSequence bufferCharSequence = new BufferCharSequence();
    private final ByteStringMap byteStringMap = new ByteStringMap(16);

    public void decode(ByteBuffer buffer, MutablePrice price) {
        visitor.withPrice(price);
        bufferCharSequence.of(buffer);
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
                } else if (PriceBytes.INSTRUMENT.equals(name)) {
                    // TODO do NOT allocate - lookup from a cache
                    price.instrument = byteStringMap.getOrCreate(buffer, offset, len);
                    name = null;
                } else if (PriceBytes.QUANTITY.equals(name)) {
                    long qty = ARITHMETIC.parse(bufferCharSequence, offset, offset + len);
                    price.setQuantity(depth, side, qty);
                    name = null;
                } else if (PriceBytes.PRICE.equals(name)) {
                    long px = ARITHMETIC.parse(bufferCharSequence, offset, offset + len);
                    price.setPrice(depth++, side, px);
                    name = null;
                } else {
                    throw new IllegalArgumentException("Unexpected string @ "
                            + offset
                            + " '"
                            + AsciiBufferUtil.getString(buffer, offset, len)
                            + "'");
                }
            }
        }

        @Override
        public void onNumber(ByteBuffer buffer, int offset, int len) {
            if (name == null) {
                throw new IllegalArgumentException("Encountered a number when attribute not set");
            } else if (PriceBytes.TIMESTAMP.equals(name)) {
                price.timestamp = AsciiBufferUtil.getLong(buffer, offset, len);
                name = null;
            } else {
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
            } else {
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
            } else if (PriceBytes.SELL.equals(name)) {
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

    private class BufferCharSequence implements CharSequence {

        private ByteBuffer buffer;

        void of(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public int length() {
            return buffer.remaining();
        }

        @Override
        public char charAt(int index) {
            return (char)buffer.get(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

}
