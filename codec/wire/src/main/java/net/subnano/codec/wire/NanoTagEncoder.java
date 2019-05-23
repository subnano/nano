package net.subnano.codec.wire;

/**
 * NanoEncoder works at a higher level than the BufferEncoders.
 * This encoder encodes the tag, the type and the value altogether.
 */
public class NanoTagEncoder implements WireEncoder {

    private final BufferEncoder bufferEncoder;

    public NanoTagEncoder(BufferEncoder bufferEncoder) {
        this.bufferEncoder = bufferEncoder;
    }

    public void addByte(int tag, byte value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.BYTE, tag));
        bufferEncoder.addByte(value);
    }

    public void addShort(int tag, short value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.SHORT, tag));
        bufferEncoder.addShort(value);
    }

    public void addInt(int tag, int value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.INT, tag));
        bufferEncoder.addInt(value);
    }

    public void addLong(int tag, long value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.LONG, tag));
        bufferEncoder.addLong(value);
    }

    public void addDouble(int tag, double value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.DOUBLE, tag));
        bufferEncoder.addDouble(value);
    }

    public void addString(int tag, String value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.STRING, tag));
        int len = value == null ? 0 : value.length();
        if (len > WireType.MAX_STRING_LEN) {
            throw new IllegalArgumentException("String value exceeds maximum length");
        }
        bufferEncoder.addByte((byte)len);
        bufferEncoder.addBytes(value);
    }

    public void addText(int tag, String value) {
        bufferEncoder.addShort(WireUtil.encodeTag(WireType.TEXT, tag));
        int len = value == null ? 0 : value.length();
        if (len > WireType.MAX_TEXT_LEN) {
            throw new IllegalArgumentException("Text value exceeds maximum length");
        }
        bufferEncoder.addShort((short)len);
        bufferEncoder.addBytes(value);
    }
}
