package net.nanofix.message.selector;

import net.nanofix.field.StringField;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.NanoFIXMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 06:20
 */
public class FieldSelectorTest {

    @Test
    public void testNullConstructor() {
        new FieldSelector(null);
    }

    @Test
    public void testIsSelected() throws Exception {
        //FIXMessage msg = Mockito.mock(FIXMessage.class);
        FIXMessage msg = new NanoFIXMessage(null);
        //TODO when(msg.getFieldValue(11)).thenReturn("abc");
        assertThat(new FieldSelector(new StringField(11, "abc")).select(msg)).isTrue();
    }
}
