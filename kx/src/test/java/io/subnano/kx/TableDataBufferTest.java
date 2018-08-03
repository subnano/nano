package io.subnano.kx;

import kx.c.Flip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
class TableDataBufferTest {

    private TableDataBuffer tableDataBuffer;

    @BeforeEach
    void setUp() {
        tableDataBuffer = TableDataBuffer.fromSchema(TestSchema.SCHEMA);
    }

    @Test
    void fromSchema() {
        Flip flip = tableDataBuffer
                .reset()
                .addString("1")
                .addShort((short) 2)
                .addInt(3)
                .addLong(4L)
                .addFloat(5.0f)
                .addDouble(6.0d)
                .addByte((byte) 7)
                .addChar('8')
                .addBoolean(true)
                .flip();
        assertThat(flip.x).isEqualTo(TestSchema.COLUMN_NAMES);
        assertThat(flip.y).isEqualTo(new Object[] {
                new String[]{"1"},
                new short[]{2},
                new int[]{3},
                new long[]{4L},
                new float[]{5.0f},
                new double[]{6.0d},
                new byte[]{7},
                new char[]{'8'},
                new boolean[]{true},
        });
    }
}