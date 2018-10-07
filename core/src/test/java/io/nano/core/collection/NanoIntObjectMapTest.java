package io.nano.core.collection;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class NanoIntObjectMapTest {

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

    @Test
    void iterator() {
        IntObjectMap<String> map = makeMap(32, 0.75f);
        map.put(1, "11");
        map.put(2, "22");
        map.put(3, "33");
        Map<Integer, String> newMap = Mockito.mock(Map.class);
        IntIterator iterator = map.iterator();
        int key;
        while ((key = iterator.nextKey()) != -1) {
            newMap.put(key, map.get(key));
        }
        verify(newMap).put(1, "11");
        verify(newMap).put(2, "22");
        verify(newMap).put(3, "33");
        verifyNoMoreInteractions(newMap);
    }

}