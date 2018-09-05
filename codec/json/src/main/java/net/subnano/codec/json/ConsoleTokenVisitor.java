package net.subnano.codec.json;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class ConsoleTokenVisitor implements TokenVisitor {

    @Override
    public void startObject(ByteBuffer buffer, int offset) {
        System.out.println("startObject(" + offset + ")");
    }

    @Override
    public void endObject(ByteBuffer buffer, int offset) {
        System.out.println("endObject(" + offset + ")");
    }

    @Override
    public void startArray(ByteBuffer buffer, int offset) {
        System.out.println("startArray(" + offset + ")");
    }

    @Override
    public void endArray(ByteBuffer buffer, int offset) {
        System.out.println("endArray(" + offset + ")");
    }

    @Override
    public void comma(ByteBuffer buffer, int offset) {
        System.out.println("comma(" + offset + ")");
    }

    @Override
    public void colon(ByteBuffer buffer, int offset) {
        System.out.println("colon(" + offset + ")");
    }

    @Override
    public void onString(ByteBuffer buffer, int offset, int len) {
        System.out.println("onString(" + offset + ")");
    }

    @Override
    public void onNumber(ByteBuffer buffer, int offset, int len) {
        System.out.println("onNumber(" + offset + ")");
    }
}
