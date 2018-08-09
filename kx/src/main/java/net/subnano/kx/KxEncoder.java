package net.subnano.kx;

/**
 * @author Mark Wardell
 */
public interface KxEncoder<T> {

    void encode(T anObject, TableDataBuffer buffer);

}
