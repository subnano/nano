package net.nanofix.message;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.collection.IntObjectMap;
import io.nano.core.collection.NanoIntObjectMap;

import java.nio.ByteBuffer;

public class LogonMessageReader2 extends MessageHeaderReader2 implements FIXMessageVisitor2 {

    // The Boolean wrapper never creates objects so is safe to use
    private boolean encryptMethod = false;
    private int heartBeatInterval = 0;
    private boolean resetSeqNumFlag = false;

    private IntObjectMap<ValueReader> tagReaders = new NanoIntObjectMap<>(16, 0.75f);

    public LogonMessageReader2() {
        super(new NanoMessageHeader());
        tagReaders.put(35, super::onMsgType);
        tagReaders.put(34, super::onMsgSeqNum);
        tagReaders.put(49, super::onSenderCompID);
        tagReaders.put(56, super::onTargetCompID);
        tagReaders.put(52, super::onSendingTime);
        tagReaders.put(98, this::onEncryptMethod);
        tagReaders.put(108, this::onHeartBtInt);
        tagReaders.put(141, this::onResetSeqNumFlag);
    }

    private void onEncryptMethod(ByteBuffer buffer, int offset, int len) {
        encryptMethod = ByteBufferUtil.toBoolean(buffer, offset);
    }

    private void onHeartBtInt(ByteBuffer buffer, int offset, int len) {
        heartBeatInterval = AsciiBufferUtil.getInt(buffer, offset, len);
    }

    private void onResetSeqNumFlag(ByteBuffer buffer, int offset, int len) {
        resetSeqNumFlag = ByteBufferUtil.toBoolean(buffer, offset);
    }

    @Override
    public boolean onTag(ByteBuffer buffer, int tag, int valueOffset, int valueLen) {
        ValueReader valueReader = tagReaders.get(tag);
        if (valueReader == null)
            return false;
        valueReader.onValue(buffer, valueOffset, valueLen);
        return true;

//        boolean handled = false;
//        if (tag == Tags.EncryptMethod) {
//            encryptMethod = ByteBufferUtil.toBoolean(buffer, valueOffset);
//            handled = true;
//        } else if (tag == Tags.HeartBtInt) {
//            heartBeatInterval = AsciiBufferUtil.getInt(buffer, valueOffset, valueLen);
//            handled = true;
//        } else if (tag == Tags.ResetSeqNumFlag) {
//            resetSeqNumFlag = ByteBufferUtil.toBoolean(buffer, valueOffset);
//            handled = true;
//        }
//        else {
//            handled = super.onTag(buffer, tag, valueOffset, valueLen);
//        }
//        return handled;
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

    @Override
    public void clear() {
        super.clear();
    }
}
