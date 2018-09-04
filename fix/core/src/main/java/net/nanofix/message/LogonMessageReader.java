package net.nanofix.message;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.TagBytes;

import java.nio.ByteBuffer;

public class LogonMessageReader extends MessageHeaderReader implements FIXMessageVisitor {

    // The Boolean wrapper never creates objects so is safe to use
    private boolean encryptMethod = false;
    private int heartBeatInterval = 0;
    private boolean resetSeqNumFlag = false;

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
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.HeartBtInt)) {
            heartBeatInterval = AsciiBufferUtil.getInt(buffer, valueIndex, valueLen);
            handled = true;
        } else if (ByteBufferUtil.hasBytes(buffer, tagIndex, TagBytes.ResetSeqNumFlag)) {
            resetSeqNumFlag = ByteBufferUtil.toBoolean(buffer, valueIndex, valueLen);
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

    public boolean encryptMethod() {
        return encryptMethod;
    }

    public int heartBeatInterval() {
        return heartBeatInterval;
    }

    public boolean resetSeqNumFlag() {
        return resetSeqNumFlag;
    }
}
