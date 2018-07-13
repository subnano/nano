package io.nano.core.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NanoIntIntMapTest {

    private final static float[] FILL_FACTORS = { 0.25f, 0.5f, 0.75f, 0.9f, 0.99f };

    protected IntIntMap makeMap( final int size, final float fillFactor ) {
        return new NanoIntIntMap(size, fillFactor);
    }

    public void testPut()
    {
        for ( final float ff : FILL_FACTORS )
            testPutHelper( ff );
    }

    private void testPutHelper( final float fillFactor )
    {
        final IntIntMap map = makeMap(100, fillFactor);
        for ( int i = 0; i < 100000; ++i )
        {
            assertEquals(0, map.put(i, i) );
            assertEquals(i + 1, map.size());
            assertEquals(i, map.get( i ));
        }
        //now check the final state
        for ( int i = 0; i < 100000; ++i )
            assertEquals(i, map.get( i ));
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void get() {
    }

    @Test
    void put() {
    }

    @Test
    void remove() {
    }

    @Test
    void size() {
    }
}