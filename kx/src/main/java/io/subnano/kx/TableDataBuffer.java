package io.subnano.kx;

import kx.c;
import kx.c.Flip;

import java.util.Date;

/**
 * Not so much a buffer as a utility method to access the Object[][]
 */
public class TableDataBuffer {

    // We may want to support writing multiple rows
    private final int rowIndex = 0;
    // pointer to column to be updated
    private int colIndex = 0;
    private final Object[] tableData;
    private final Flip flip;

    TableDataBuffer(String[] columnNames, Object[] tableSchema) {
        this.tableData = tableSchema;
        this.flip = new Flip(new c.Dict(columnNames, tableSchema));
    }

    public static TableDataBuffer fromSchema(KxSchema schema) {
        return new TableDataBuffer(schema.columnNames(), schema.data());
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

    public TableDataBuffer addFloat(float value) {
        Object colData = tableData[colIndex++];
        ((float[]) colData)[rowIndex] = value;
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
            throw new ClassCastException(colData.getClass() + " cannot be cast to String[] when updating column " + colIndex);
        ((String[]) colData)[rowIndex] = value;
        colIndex++;
        return this;
    }

    public void addTimestamp(long timestamp) {
        Object colData = tableData[colIndex];
        if (!(colData instanceof Date[]))
            throw new ClassCastException(colData.getClass() + " cannot be cast to Date[] when updating column " + colIndex);
        Date date = ((Date[]) colData)[rowIndex];
        if (date == null) {
            date = new Date();
            ((Date[]) colData)[rowIndex] = date;
        }
        date.setTime(timestamp);
        colIndex++;
    }

    Flip flip() {
        return flip;
    }
}


