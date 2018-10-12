package net.subnano.codec.wire;

import io.nano.core.util.Maths;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.subnano.codec.wire.WireType.BYTE;
import static net.subnano.codec.wire.WireType.SHORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WireUtilTest {

    private static final int MAX_TAG = 4096;

    @BeforeEach
    void setUp() {
    }

    @Test
    void bitLengths() {
        for (int i = 0; i<=32; i++) {
            System.out.printf("2^%d = %d\n", i, Maths.pow(2, i));
         //   System.out.printf("2^%d = %f\n", i, Math.pow((double)i, 2.0d));
        }
    }

    @Test
    void custom() {
        int packed = WireUtil.encodeTag(BYTE, 256);
        String binaryString = Integer.toBinaryString(packed);
        int binaryStringLen = binaryString.length();
        assertThat(WireUtil.decodeTag(packed)).isEqualTo(256);
        assertThat(WireUtil.decodeType(packed)).isEqualTo((byte)0);
    }

    @Test
    void encodeAndDecodeTagAndType() {
        for (int tag = 0; tag<MAX_TAG; tag++) {
            for (byte type : WireType.values()) {
                int  packed = WireUtil.encodeTag(type, tag);
                Description description = new TextDescription("Type:%s Tag:%d", type, tag);
                assertThat(WireUtil.decodeTag(packed)).as(description).isEqualTo((short)tag);
                assertThat(WireUtil.decodeType(packed)).as(description).isEqualTo(type);
            }
        }
    }
}