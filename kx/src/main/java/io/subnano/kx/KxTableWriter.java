package io.subnano.kx;

/**
 * @author Mark Wardell
 */
public interface KxTableWriter<T> {

    void write(T record);

}
