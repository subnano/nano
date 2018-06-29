package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 19:24
 */
public class NegativeSelectorTest {

    @Test
    public void testIsSelected() throws Exception {
        MessageSelector selector = mock(MessageSelector.class);
        when(selector.select(any(FIXMessage.class))).thenReturn(true);
        assertThat(new NegativeSelector(selector).select(mock(FIXMessage.class))).isFalse();
    }
}
