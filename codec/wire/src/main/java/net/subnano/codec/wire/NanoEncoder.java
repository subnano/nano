package net.subnano.codec.wire;

/**
 * NanoEncoder works at a higher level than the BufferEncoders.
 * This encoder encodes the tag, the type and the value altogether.
 */
public class NanoEncoder implements WireEncoder {

    private final BufferEncoder bufferEncoder;

    public NanoEncoder(BufferEncoder bufferEncoder) {
        this.bufferEncoder = bufferEncoder;
    }

    public void addByte(int tag, byte value) {
        bufferEncoder.addByte((byte)WireType.Byte.ordinal());
        bufferEncoder.addShort(tag);
        bufferEncoder.addByte(value);
    }

    public void addInt(int tag, int value) {
        bufferEncoder.addByte((byte)WireType.Int.ordinal());
        bufferEncoder.addShort(tag);
        bufferEncoder.addInt(value);
    }

    public void addLong(int tag, long value) {
        bufferEncoder.addByte((byte)WireType.Long.ordinal());
        bufferEncoder.addShort(tag);
        bufferEncoder.addLong(value);
    }

    public void addString(int tag, String value) {
        bufferEncoder.addByte((byte)WireType.String.ordinal());
        bufferEncoder.addShort(tag);
        bufferEncoder.addString(value);
    }
}
