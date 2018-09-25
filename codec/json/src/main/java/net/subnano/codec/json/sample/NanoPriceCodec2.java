package net.subnano.codec.json.sample;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.collection.ByteStringArray;
import io.nano.core.collection.IntObjectMap;
import io.nano.core.collection.NanoIntObjectMap;
import io.nano.core.lang.ByteString;
import io.nano.core.util.ByteArrayUtil;
import net.subnano.codec.json.JsonByteParser;
import net.subnano.codec.json.JsonVisitor;
import org.decimal4j.api.DecimalArithmetic;
import org.decimal4j.immutable.Decimal4f;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class NanoPriceCodec2 {

    private static final DecimalArithmetic ARITHMETIC = Decimal4f.DEFAULT_ARITHMETIC;

    private final JsonByteParser parser = new JsonByteParser();
    private final JsonPriceVisitor visitor = new JsonPriceVisitor();
    private final BufferCharSequence bufferCharSequence = new BufferCharSequence();
    private final ByteStringArray byteStringArray = new ByteStringArray(16);

    public void decode(ByteBuffer buffer, MutablePrice price) {
        visitor.withPrice(price);
        bufferCharSequence.of(buffer.array());
        price.clear();
        parser.parse(buffer, visitor);
    }

    private class JsonPriceVisitor implements JsonVisitor {

        private final IntObjectMap<ByteString> nameMap = new NanoIntObjectMap<>(16, 0.75f);
        private MutablePrice price;
        private int attribute = 0;
        private MutablePrice.MutableLevel[] side;
        private int depth = 0;

        public JsonPriceVisitor() {
            addAttribute(PriceBytes.EVENT);
            addAttribute(PriceBytes.TIMESTAMP);
            addAttribute(PriceBytes.SUCCESS);
            addAttribute(PriceBytes.INSTRUMENT);
            addAttribute(PriceBytes.BUY);
            addAttribute(PriceBytes.SELL);
            addAttribute(PriceBytes.QUANTITY);
            addAttribute(PriceBytes.PRICE);
        }

        private void addAttribute(ByteString byteString) {
            int key = ByteArrayUtil.hash(byteString.bytes(), 0, byteString.length());
            //System.out.println(byteString.toString() + " hash = " + key);
            nameMap.put(key, byteString);
        }

        public void withPrice(MutablePrice price) {
            this.price = price;
        }

        @Override
        public void onString(ByteBuffer buffer, int offset, int len) {
            switch (attribute) {
                // if name is null then we record the attribute
                // else if name is set then we have an attribute value
                case 0:
                    attribute = getAttributeHash(buffer, offset, len);
                    break;
                // Ignore the confirmation of 'event' == 'price'
                // This needs to be determined to obtain correct JSON codec in advance
                case 571700625: // event
                    attribute = 0;
                    break;
                case 977569014: // instrument
                    price.instrument = byteStringArray.getOrCreate(buffer, offset, len);
                    attribute = 0;
                    break;
                case 740639257: // quantity
                    long qty = ARITHMETIC.parse(bufferCharSequence, offset, offset + len);
                    price.setQuantity(depth, side, qty);
                    attribute = 0;
                    break;
                case 660786995: // price
                    long px = ARITHMETIC.parse(bufferCharSequence, offset, offset + len);
                    price.setPrice(depth++, side, px);
                    attribute = 0;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected string @ "
                            + offset
                            + " '"
                            + AsciiBufferUtil.getString(buffer, offset, len)
                            + "'");
            }
        }

        @Override
        public void onNumber(ByteBuffer buffer, int offset, int len) {
            if (attribute == 0) {
                throw new IllegalArgumentException("Encountered a number when attribute not set");
            } else if (PriceBytes.TIMESTAMP.hashCode() == attribute) {
                price.timestamp = AsciiBufferUtil.getLong(buffer, offset, len);
                attribute = 0;
            } else {
                throw new IllegalArgumentException("Unexpected number");
            }
        }

        @Override
        public void onError(ByteBuffer buffer, int offset, String cause) {

        }

        @Override
        public void onBoolean(ByteBuffer buffer, int offset, boolean value) {
            if (attribute == 0) {
                throw new IllegalArgumentException("Encountered a number when attribute not set");
            }
            if (PriceBytes.SUCCESS.hashCode() == attribute) {
                price.success = value;
                attribute = 0;
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
            if (PriceBytes.BUY.hashCode() == attribute) {
                side = price.bids;
                attribute = 0;
            } else if (PriceBytes.SELL.hashCode() == attribute) {
                side = price.asks;
                attribute = 0;
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

        private int getAttributeHash(ByteBuffer buffer, int offset, int len) {
            int hash = ByteBufferUtil.hash(buffer, offset, len);
            switch (hash) {
                case 571700625:
                case -233242563:
                case 465923568:
                case 977569014:
                case -28794613:
                case 1946508888:
                case 740639257:
                case 660786995:
                break;
                default:
                    throw new IllegalArgumentException("Unsupported attribute or JSON schema invalid");
            }
            return hash;
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

        private byte[] bytes;

        void of(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int length() {
            return bytes.length;
        }

        @Override
        public char charAt(int index) {
            return (char)bytes[index];
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

}
