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
        bufferEncoder.addByte(WireType.BYTE);
        bufferEncoder.addShort((short) tag);
        bufferEncoder.addByte(value);
    }

    public void addShort(int tag, short value) {
        bufferEncoder.addByte(WireType.SHORT);
        bufferEncoder.addShort((short) tag);
        bufferEncoder.addShort(value);
    }

    public void addInt(int tag, int value) {
        bufferEncoder.addByte(WireType.INT);
        bufferEncoder.addShort((short) tag);
        bufferEncoder.addInt(value);
    }

    public void addLong(int tag, long value) {
        bufferEncoder.addByte(WireType.LONG);
        bufferEncoder.addShort((short) tag);
        bufferEncoder.addLong(value);
    }

    public void addDouble(int tag, double value) {
        bufferEncoder.addByte(WireType.LONG);
        bufferEncoder.addShort((short) tag);
        bufferEncoder.addDouble(value);
    }

    public void addString(int tag, String value) {
        bufferEncoder.addByte(WireType.STRING);
        bufferEncoder.addShort((short) tag);
        bufferEncoder.addString(value);
    }
}
