package net.subnano.codec.wire;

public interface TagVisitor<T> {

    /**
     * Invoked as every tag in a buffer is encountered
     * @param source the source buffer
     * @param tag tag number as an integer
     * @param type type type of the tag
     * @param offset the pointer into the source or buffer offset
     * @param len the length of data to be read
     */
    void onTag(T source, int tag, byte type, int offset, int len);
}
