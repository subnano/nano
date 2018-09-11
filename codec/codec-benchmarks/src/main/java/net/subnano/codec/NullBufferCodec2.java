package net.subnano.codec;

import net.subnano.codec.json.JsonByteParser2;
import net.subnano.codec.json.JsonVisitor;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class NullBufferCodec2 {

    private final JsonByteParser2 parser = new JsonByteParser2();
    private final JsonVisitor visitor = new NullJsonVisitor();

    public void decode(ByteBuffer buffer) {
        parser.parse(buffer, visitor);
    }

}
