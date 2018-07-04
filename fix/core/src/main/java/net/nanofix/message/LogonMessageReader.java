package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.lang.MutableInteger;
import net.nanofix.util.TagBytes;

import java.nio.ByteBuffer;

public class LogonMessageReader extends MessageHeaderReader implements FIXMessageVisitor {

    // The Boolean wrapper never creates objects so is safe to use
    private MutableBoolean encryptMethod = new MutableBoolean();
    private MutableInteger heartBeatInterval = new MutableInteger();
    private MutableBoolean resetSeqNumFlag = new MutableBoolean();

    public LogonMessageReader() {
        super(new NanoMessageHeader());
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        boolean handled = false;

        // possibly let parent handle this tag
        if (!super.complete()) {
            handled = super.onTag(buffer, tagIndex, tagLen, valueLen);
            if (handled) return true;
        }

        // populate here
        int valueIndex = tagIndex + tagLen + 1;
        if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.EncryptMethod)) {
            encryptMethod = ByteBufferUtil.toBoolean(buffer, valueIndex, valueLen);
            handled = true;
        }
        else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.HeartBtInt)) {
            heartBeatInterval = ByteBufferUtil.toInt(buffer, valueIndex, valueLen);
            handled = true;
        }
        return handled;
    }

    @Override
    public void onError(int index, String message) {

    }

    @Override
    public boolean complete() {
        return false;
    }

    public boolean isEncryptMethod() {
        return encryptMethod;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }
}
