package io.nano.core.collection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NanoIntObjectMapTest {

    private final static float[] FILL_FACTORS = {0.25f, 0.5f, 0.75f, 0.9f, 0.99f};
    private static final int COUNT = 500;

    private IntObjectMap<String> makeMap(final int size, final float fillFactor) {
        return new NanoIntObjectMap<>(size, fillFactor);
    }

    private void mapTestHelper(final float fillFactor) {
        final IntObjectMap<String> map = makeMap(128, fillFactor);
        for (int i = 0; i < COUNT; ++i) {
            String value = String.valueOf(i);
            assertThat(map.put(i, value)).as("put(%d,%d)", i, i).isEqualTo(null);
            assertThat(map.size()).as("size()").isEqualTo(i + 1);
            assertThat(map.get(i)).as("get(%d)", i).isEqualTo(value);
        }
        // check the final state
        for (int i = 0; i < COUNT; ++i) {
            String value = String.valueOf(i);
            assertThat(map.get(i)).as("get(%d)", i).isEqualTo(value);
        }

        // now check the removal
        for (int i = 0; i < COUNT; ++i) {
            String value = String.valueOf(i);
            assertThat(map.remove(i)).as("remove(%d)", i).isEqualTo(value);
            assertThat(map.remove(i)).as("remove(%d)", i).isEqualTo(null);
        }
        assertThat(map.size()).as("size()").isZero();
    }

    @Test
    void putAndGetCombined() {
        for (final float ff : FILL_FACTORS) {
            mapTestHelper(ff);
        }
    }

}