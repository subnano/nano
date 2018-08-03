package io.subnano.kx;

/**
 * @author Mark Wardell
 */
public class TestSchema {

    static final String TABLE_NAME = "mytable";

    static final String[] COLUMN_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};

    static final Object[] TABLE_DATA = {
            new String[]{null},
            new short[]{0},
            new int[]{0},
            new long[]{0L},
            new float[]{0.0f},
            new double[]{0.0d},
            new byte[]{0},
            new char[]{0},
            new boolean[]{false},
    };

    static final KxSchema SCHEMA = new DefaultKxSchema.Builder()
            .forTable(TestSchema.TABLE_NAME)
            .addColumn("A", ColumnType.String)
            .addColumn("B", ColumnType.Short)
            .addColumn("C", ColumnType.Int)
            .addColumn("D", ColumnType.Long)
            .addColumn("E", ColumnType.Float)
            .addColumn("F", ColumnType.Double)
            .addColumn("G", ColumnType.Byte)
            .addColumn("H", ColumnType.Char)
            .addColumn("I", ColumnType.Boolean)
            .build();

}
