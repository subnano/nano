package net.subnano.codec.wire;

import io.nano.core.lang.ByteString;
import io.nano.core.util.ByteArrayUtil;

import java.util.function.Consumer;

/**
 * NanoEncoder works at a higher level than the BufferEncoders.
 * This encoder encodes the tag, the type and the value altogether.
 */
public class NanoEncoder implements WireEncoder {

    private final BufferEncoder bufferEncoder;

    public NanoEncoder(BufferEncoder bufferEncoder) {
        this.bufferEncoder = bufferEncoder;
    }

    public void streamId(byte streamId) {
        // TODO need to ensure this happens first and not just anywhere
        bufferEncoder.addByte(streamId);
    }

    public void addByte(byte value) {
        bufferEncoder.addByte(WireType.BYTE);
        bufferEncoder.addByte(value);
    }

    public void addShort(short value) {
        bufferEncoder.addByte(WireType.SHORT);
        bufferEncoder.addShort(value);
    }

    public void addInt(int value) {
        bufferEncoder.addByte(WireType.INT);
        bufferEncoder.addInt(value);
    }

    public void addLong(long value) {
        bufferEncoder.addByte(WireType.LONG);
        bufferEncoder.addLong(value);
    }

    public void addDouble(double value) {
        bufferEncoder.addByte(WireType.DOUBLE);
        bufferEncoder.addDouble(value);
    }

    public void addString(ByteString value) {
        bufferEncoder.addByte(WireType.STRING);
        int len = value == null ? 0 : value.length();
        if (len > WireType.MAX_STRING_LEN) {
            throw new IllegalArgumentException("String value exceeds maximum length");
        }
        bufferEncoder.addByte((byte)len);
        bufferEncoder.addBytes(value.bytes());
    }

    /**
     * Cannot avoid object allocation if we want to support large text strings
     * Advise not to use if performance is a consideration
     * @param value
     */
    public void addText(String value) {
        bufferEncoder.addByte(WireType.TEXT);
        int len = value == null ? 0 : value.length();
        if (len > WireType.MAX_TEXT_LEN) {
            throw new IllegalArgumentException("Text value exceeds maximum length");
        }
        bufferEncoder.addShort((short)len);
        bufferEncoder.addBytes(ByteArrayUtil.asByteArray(value));
    }

    public void addLongArray(int length, long[] longValues) {
        bufferEncoder.addByte(WireType.LONG_ARRAY);
        bufferEncoder.addShort((short)length);
        for (long value : longValues) {
            bufferEncoder.addLong(value);
        }
    }

    /**
     * Limited to 64k entries
     * @param length
     * @param arrayEncoder
     */
    public void addLongArray(int length, Consumer<BufferEncoder> arrayEncoder) {
        bufferEncoder.addByte(WireType.LONG_ARRAY);
        bufferEncoder.addShort((short)length);
        arrayEncoder.accept(bufferEncoder);
    }
}
