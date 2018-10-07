package net.subnano.kx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
public class DefaultKxSchemaTest {

    private KxSchema schema;

    @BeforeEach
    void setUp() {
        this.schema = new DefaultKxSchema.Builder()
                .table(TestSchema.TABLE_NAME)
                .addColumn("A", ColumnType.Boolean)
                .addColumn("B", ColumnType.Byte)
                .addColumn("C", ColumnType.Char)
                .addColumn("D", ColumnType.Short)
                .addColumn("E", ColumnType.Int)
                .addColumn("F", ColumnType.Long)
                .addColumn("G", ColumnType.Double)
                .addColumn("H", ColumnType.String)
                .addColumn("I", ColumnType.DateTime)
                .addColumn("J", ColumnType.Timestamp)
                .addColumn("K", ColumnType.Timespan)
                .addColumn("L", ColumnType.CharArray)
                .batchSize(2)
                .build();
    }

    @Test
    void tableName() {
        assertThat(schema.tableName()).isSameAs(TestSchema.TABLE_NAME);
    }

    @Test
    void columnNames() {
        assertThat(schema.columnNames()).isEqualTo(TestSchema.COLUMN_NAMES);
    }

    @Test
    void data() {
        assertThat(schema.data()).isEqualTo(TestSchema.EMPTY_TABLE_DATA);
    }

}