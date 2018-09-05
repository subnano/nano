package net.subnano.codec.json;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public interface TokenVisitor {

    void startObject(ByteBuffer buffer, int offset);

    void endObject(ByteBuffer buffer, int offset);

    void startArray(ByteBuffer buffer, int offset);

    void endArray(ByteBuffer buffer, int offset);

    void comma(ByteBuffer buffer, int offset);

    void colon(ByteBuffer buffer, int offset);

    void onString(ByteBuffer buffer, int offset, int len);

    void onNumber(ByteBuffer buffer, int offset, int len);
}
