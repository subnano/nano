package net.subnano.kx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
class DefaultKxSchemaTest {

    private KxSchema schema;

    @BeforeEach
    void setUp() {
        this.schema = new DefaultKxSchema.Builder()
                .forTable(TestSchema.TABLE_NAME)
                .addColumn("A", ColumnType.String)
                .addColumn("B", ColumnType.Short)
                .addColumn("C", ColumnType.Int)
                .addColumn("D", ColumnType.Long)
                .addColumn("E", ColumnType.Double)
                .addColumn("F", ColumnType.Byte)
                .addColumn("G", ColumnType.Char)
                .addColumn("H", ColumnType.Boolean)
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
        assertThat(schema.data()).isEqualTo(TestSchema.TABLE_DATA);
    }

}