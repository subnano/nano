package net.subnano.kx;

import kx.c.Timespan;

/**
 * @author Mark Wardell
 */
public class TestSchema {

    static final String TABLE_NAME = "mytable";

    static final String[] COLUMN_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H"};

    static final Object[] TABLE_DATA = {
            new String[]{null},
            new short[]{0},
            new int[]{0},
            new long[]{0L},
            new double[]{0.0d},
            new byte[]{0},
            new char[]{0},
            new boolean[]{false},
            new Timespan[]{new Timespan(132)}
    };

    static final KxSchema SCHEMA = new DefaultKxSchema.Builder()
            .table(TestSchema.TABLE_NAME)
            .addColumn("A", ColumnType.String)
            .addColumn("B", ColumnType.Short)
            .addColumn("C", ColumnType.Int)
            .addColumn("D", ColumnType.Long)
            .addColumn("E", ColumnType.Double)
            .addColumn("F", ColumnType.Byte)
            .addColumn("G", ColumnType.Char)
            .addColumn("H", ColumnType.Boolean)
            .addColumn("I", ColumnType.Timespan)
            .build();

}
