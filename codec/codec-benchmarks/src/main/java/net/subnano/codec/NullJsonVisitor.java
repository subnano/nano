package net.subnano.codec;

import net.subnano.codec.json.JsonVisitor;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class NullJsonVisitor implements JsonVisitor {
    @Override
    public void startObject(ByteBuffer buffer, int offset) {
        // NO-OP
    }

    @Override
    public void endObject(ByteBuffer buffer, int offset) {
        // NO-OP
    }

    @Override
    public void startArray(ByteBuffer buffer, int offset) {
        // NO-OP
    }

    @Override
    public void endArray(ByteBuffer buffer, int offset) {
        // NO-OP
    }

    @Override
    public void comma(ByteBuffer buffer, int offset) {
        // NO-OP
    }

    @Override
    public void colon(ByteBuffer buffer, int offset) {
        // NO-OP
    }

    @Override
    public void onString(ByteBuffer buffer, int offset, int len) {
        // NO-OP
    }

    @Override
    public void onNumber(ByteBuffer buffer, int offset, int len) {
        // NO-OP
    }

    @Override
    public void onError(ByteBuffer buffer, int offset, String cause) {
        // NO-OP
    }

    @Override
    public void onBoolean(ByteBuffer buffer, int offset, boolean value) {
        // NO-OP
    }
}
