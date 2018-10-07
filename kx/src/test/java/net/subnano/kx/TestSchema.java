package net.subnano.kx;

import kx.c.Timespan;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Mark Wardell
 */
public class TestSchema {

    static final String TABLE_NAME = "mytable";

    static final String[] COLUMN_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};

    private static final int BATCH_SIZE = 2;

    static final KxSchema SCHEMA = new DefaultKxSchema.Builder()
            .table(TestSchema.TABLE_NAME)
            .addColumn(COLUMN_NAMES[0], ColumnType.Boolean)
            .addColumn(COLUMN_NAMES[1], ColumnType.Byte)
            .addColumn(COLUMN_NAMES[2], ColumnType.Char)
            .addColumn(COLUMN_NAMES[3], ColumnType.Short)
            .addColumn(COLUMN_NAMES[4], ColumnType.Int)
            .addColumn(COLUMN_NAMES[5], ColumnType.Long)
            .addColumn(COLUMN_NAMES[6], ColumnType.Double)
            .addColumn(COLUMN_NAMES[7], ColumnType.String)
            .addColumn(COLUMN_NAMES[8], ColumnType.DateTime)
            .addColumn(COLUMN_NAMES[9], ColumnType.Timestamp)
            .addColumn(COLUMN_NAMES[10], ColumnType.Timespan)
            .addColumn(COLUMN_NAMES[11], ColumnType.CharArray)
            .batchSize(BATCH_SIZE)
            .build();

    static final Object[] EMPTY_TABLE_DATA = {
            new boolean[BATCH_SIZE],
            new byte[BATCH_SIZE],
            new char[BATCH_SIZE],
            new short[BATCH_SIZE],
            new int[BATCH_SIZE],
            new long[BATCH_SIZE],
            new double[BATCH_SIZE],
            new String[BATCH_SIZE],
            new Date[BATCH_SIZE],
            new Timestamp[BATCH_SIZE],
            new Timespan[BATCH_SIZE],
            new char[BATCH_SIZE][]
    };


}
