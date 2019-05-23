package net.subnano.codec.wire;

public interface WireVisitor {

    /**
     * Invoked as every field in a buffer is encountered
     * @param source the source buffer
     * @param type type type of the field
     * @param offset the pointer into the source or buffer offset
     * @param len the length of data to be read
     */
    void onTag(BufferReader source, byte type, int offset, int len);

}
