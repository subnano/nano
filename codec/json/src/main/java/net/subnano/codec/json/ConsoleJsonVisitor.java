package net.subnano.codec.json;

import io.nano.core.buffer.AsciiBufferUtil;

import java.nio.ByteBuffer;

/**
 * An implementation of {@link JsonVisitor} that writes the data to the console.
 *
 * @author Mark Wardell
 */
public class ConsoleJsonVisitor implements JsonVisitor {

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
        String text = AsciiBufferUtil.getString(buffer, offset, len);
        System.out.println("onString(" + offset + "," + len +") \"" + text + "\"");
    }

    @Override
    public void onNumber(ByteBuffer buffer, int offset, int len) {
        String text = AsciiBufferUtil.getString(buffer, offset, len);
        System.out.println("onNumber(" + offset + "," + len +") \"" + text + "\"");
    }

    @Override
    public void onError(ByteBuffer buffer, int offset, String cause) {
        char text = (char) buffer.get(offset);
        System.out.println("onError: '" + cause +"' @ " + offset + " => \"" + text + "\"");
    }

    @Override
    public void onBoolean(ByteBuffer buffer, int offset, boolean value) {
        System.out.println("onBoolean(" + offset + "," + value +")");
    }
}
