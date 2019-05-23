package net.subnano.codec.wire;

/**
 * TODO implement WireReader?
 * WireReader types
 * Raw
 * Typed
 * Tagged
 */
public class NanoWireReader {

    private final BufferReader bufferReader;

    public NanoWireReader(BufferReader bufferReader) {
        this.bufferReader = bufferReader;
    }

    public byte streamId() {
        return bufferReader.readByte();
    }

    public long readLong() {
        byte wireType = bufferReader.readByte();
        if (WireType.LONG == wireType)
            return bufferReader.readLong();
        // TODO consider alternatives to throwing exception
        throw new IllegalArgumentException("Invalid wire type found");
    }
}
