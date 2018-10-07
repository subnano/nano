package net.subnano.kx;

import kx.c.Timespan;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Not so much a buffer as a utility method to access the Object[][]
 * Overall class hierarchy needs a review as it has evolved into something less intuitive
 * especially since the introduction of batching.
 *
 * @author Mark Wardell
 */
public class TableDataBuffer {

    private final String[] columnNames;
    private final Object[] tableData;
    private final int size;

    // pointers to row and column to be updated
    private int rowIndex = 0;
    private int colIndex = 0;

    TableDataBuffer(String[] columnNames, Object[] tableData, int size) {
        this.columnNames = columnNames;
        this.tableData = tableData;
        this.size = size;
    }

    public static TableDataBuffer fromSchema(KxSchema schema) {
        return new TableDataBuffer(schema.columnNames(), schema.data(), schema.batchSize());
    }

    /**
     * Rests column index (rename?)
     */
    public TableDataBuffer reset() {
        colIndex = 0;
        return this;
    }

    public TableDataBuffer addBoolean(boolean value) {
        Object colData = tableData[colIndex++];
        ((boolean[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addByte(byte value) {
        Object colData = tableData[colIndex++];
        ((byte[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addShort(short value) {
        Object colData = tableData[colIndex++];
        ((short[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addInt(int value) {
        Object colData = tableData[colIndex++];
        ((int[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addLong(long value) {
        Object colData = tableData[colIndex++];
        ((long[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addDouble(double value) {
        Object colData = tableData[colIndex++];
        ((double[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addChar(char value) {
        Object colData = tableData[colIndex++];
        ((char[]) colData)[rowIndex] = value;
        return this;
    }

    public TableDataBuffer addString(String value) {
        Object colData = tableData[colIndex];
        if (!(colData instanceof String[]))
            throw new ClassCastException(colData.getClass() + " cannot be cast to String[] when updating column " + columnNames[colIndex]);
        ((String[]) colData)[rowIndex] = value;
        colIndex++;
        return this;
    }

    public TableDataBuffer addDateTime(long value) {
        Object colData = tableData[colIndex];
        if (!(colData instanceof Date[]))
            throw new ClassCastException(colData.getClass() + " cannot be cast to Date[] when updating column " + columnNames[colIndex]);
        Date date = ((Date[]) colData)[rowIndex];
        if (date == null) {
            date = new Date();
            ((Date[]) colData)[rowIndex] = date;
        }
        date.setTime(value);
        colIndex++;
        return this;
    }

    public TableDataBuffer addTimestamp(long value) {
        Object colData = tableData[colIndex];
        if (!(colData instanceof Timestamp[]))
            throw new ClassCastException(colData.getClass() + " cannot be cast to Timestamp[] when updating column " + columnNames[colIndex]);
        Timestamp timestamp = ((Timestamp[]) colData)[rowIndex];
        if (timestamp == null) {
            timestamp = new Timestamp(value);
            ((Timestamp[]) colData)[rowIndex] = timestamp;
        } else {
            timestamp.setTime(value);
        }
        colIndex++;
        return this;
    }

    /**
     * Add Timespan value to buffer
     *
     * @param value Timespan long values are in nanoseconds
     */
    public TableDataBuffer addTimespan(long value) {
        Object colData = tableData[colIndex];
        if (!(colData instanceof Timespan[]))
            throw new ClassCastException(colData.getClass() + " cannot be cast to Timespan[] when updating column " + columnNames[colIndex]);
        Timespan timespan = ((Timespan[]) colData)[rowIndex];
        if (timespan == null) {
            timespan= new Timespan(value);
            ((Timespan[]) colData)[rowIndex] = timespan;
        } else {
            timespan.j = value;
        }
        colIndex++;
        return this;
    }

    /**
     * Add char[] value to buffer
     *
     * @param value char array
     */
    public TableDataBuffer addCharArray(char[] value) {
        Object colData = tableData[colIndex];
        if (!(colData instanceof char[][]))
            throw new ClassCastException(colData.getClass() + " cannot be cast to char[][] when updating column " + columnNames[colIndex]);
        char[][] charArray = ((char[][])colData);
        charArray[rowIndex] = value;
        colIndex++;
        return this;
    }

    public void completeRow() {
        rowIndex++;
    }

    public String[] columnNames() {
        return columnNames;
    }

    public Object[] tableData() {
        return tableData;
    }

    /**
     * Clear all row and column pointers
     */
    public void clear() {
        rowIndex = 0;
        colIndex = 0;
    }

    /**
     * Returns the number of records currently stored in the buffer ready to be written to Kx
     *
     * @return count as a positive integer
     */
    public int count() {
        return rowIndex;
    }

    /**
     * Returns the number of records that can be inserted into the buffer before writing to Kx
     *
     * @return size as a positive integer
     */
    public int size() {
        return size;
    }

    public boolean isFull() {
        return rowIndex == size;
    }
}


