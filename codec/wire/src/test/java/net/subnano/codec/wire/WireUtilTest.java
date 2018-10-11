package net.subnano.codec.wire;

import io.nano.core.util.Maths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WireUtilTest {

    private static final int MAX_TAG = 65535;

    @BeforeEach
    void setUp() {
    }

    @Test
    void encodeTag() {
        for (int i = 0; i<=32; i++) {
            System.out.printf("2^%d = %d\n", i, Maths.pow(2, i));
         //   System.out.printf("2^%d = %f\n", i, Math.pow((double)i, 2.0d));
        }
    }
}