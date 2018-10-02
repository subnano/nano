package net.subnano.kx;

/**
 * @author Mark Wardell
 */
public interface KxTableWriter<T> {

    /**
     * Write given record to Kx table.
     * Due to batching this write command may, or may not, actually write data to the Kx process
     *
     * @param record data to write
     * @return the number of records actually written
     */
    int write(T record);

    /**
     * Flushes any records sitting in the buffer and writes them to Kx
     *
     * @return the number of records written
     */
    int flush();
}
