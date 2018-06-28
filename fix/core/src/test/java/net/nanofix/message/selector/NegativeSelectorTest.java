package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;
import org.junit.jupiter.api.Test;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 19:24
 */
public class NegativeSelectorTest {

    @Test
    public void testNullConstructor() {
        new NegativeSelector(null);
    }

    @Test
    public void testIsSelected() throws Exception {
//        MessageSelector selector = mock(MessageSelector.class);
//        when(selector.select(any(FIXMessage.class))).thenReturn(true);
//        assertThat(new NegativeSelector(selector).select(mock(FIXMessage.class)), is(false));
    }
}
