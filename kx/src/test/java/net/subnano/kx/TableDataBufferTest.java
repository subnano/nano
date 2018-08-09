package net.subnano.kx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
class TableDataBufferTest {

    private TableDataBuffer buffer;

    @BeforeEach
    void setUp() {
        buffer = TableDataBuffer.fromSchema(TestSchema.SCHEMA);
    }

    @Test
    void fromSchema() {
        buffer
                .reset()
                .addString("1")
                .addShort((short) 2)
                .addInt(3)
                .addLong(4L)
                .addDouble(6.0d)
                .addByte((byte) 7)
                .addChar('8')
                .addBoolean(true);

        assertThat(buffer.columnNames()).isEqualTo(TestSchema.COLUMN_NAMES);
        assertThat(buffer.tableData()).isEqualTo(new Object[] {
                new String[]{"1"},
                new short[]{2},
                new int[]{3},
                new long[]{4L},
                new double[]{6.0d},
                new byte[]{7},
                new char[]{'8'},
                new boolean[]{true},
        });
    }
}