package net.subnano.kx;

import kx.c.Timespan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
public class TableDataBufferTest {

    private TableDataBuffer buffer;

    @BeforeEach
    void setUp() {
        buffer = TableDataBuffer.fromSchema(TestSchema.SCHEMA);
    }

    @Test
    void fromSchema() {
        buffer
            .reset()
            .addBoolean(true)
            .addByte((byte) 17)
            .addChar('3')
            .addShort((short) 11)
            .addInt(111)
            .addLong(1111L)
            .addDouble(1.1d)
            .addString("11111111")
            .addDateTime(1234)
            .addTimestamp(12345)
            .addTimespan(123456)
            .addCharArray("1234567".toCharArray())
        ;

        assertThat(buffer.columnNames()).isEqualTo(TestSchema.COLUMN_NAMES);
        assertThat(buffer.tableData()).isEqualTo(new Object[] {
                new boolean[]{true, false},
                new byte[]{17, 0},
                new char[]{'3', 0},
                new short[]{11, 0},
                new int[]{111, 0},
                new long[]{1111L, 0L},
                new double[]{1.1d, 0.0D},
                new String[]{"11111111", null},
                new Date[]{new Date(1234), null},
                new Timestamp[]{new Timestamp(12345), null},
                new Timespan[]{new Timespan(123456), null},
                new char[][]{ "1234567".toCharArray(), null}
        });
    }
}