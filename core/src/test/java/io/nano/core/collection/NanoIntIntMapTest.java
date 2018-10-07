package io.nano.core.collection;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class NanoIntIntMapTest {

    private final static float[] FILL_FACTORS = {0.25f, 0.5f, 0.75f, 0.9f, 0.99f};
    private static final int COUNT = 500;

    private IntIntMap makeMap(final int size, final float fillFactor) {
        return new NanoIntIntMap(size, fillFactor);
    }

    private void mapTestHelper(final float fillFactor) {
        final IntIntMap map = makeMap(128, fillFactor);
        for (int i = 0; i < COUNT; ++i) {
            assertThat(map.put(i, i)).as("put(%d,%d)", i, i).isEqualTo(-1);
            assertThat(map.size()).as("size()").isEqualTo(i + 1);
            assertThat(map.get(i)).as("get(%d)", i).isEqualTo(i);
        }

        // check the final state
        for (int i = 0; i < COUNT; ++i) {
            assertThat(map.get(i)).as("get(%d)", i).isEqualTo(i);
        }

        // now check the removal
        for (int i = 0; i < COUNT; ++i) {
            assertThat(map.remove(i)).as("remove(%d)", i).isEqualTo(i);
            assertThat(map.remove(i)).as("remove(%d)", i).isEqualTo(-1);
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
        IntIntMap map = makeMap(32, 0.75f);
        map.put(1, 11);
        map.put(2, 22);
        map.put(3, 33);
        Map<Integer, Integer> newMap = Mockito.mock(Map.class);
        IntIterator iterator = map.iterator();
        int key;
        while ((key = iterator.nextKey()) != -1) {
            newMap.put(key, map.get(key));
        }
        verify(newMap).put(1, 11);
        verify(newMap).put(2, 22);
        verify(newMap).put(3, 33);
        verifyNoMoreInteractions(newMap);
    }

}