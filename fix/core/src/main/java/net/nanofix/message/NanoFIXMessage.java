package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.message.util.ChecksumCalculator;

import java.nio.ByteBuffer;

/**
 * This class has mutable state so concurrent use is not advised.
 */
public class NanoFIXMessage implements FIXMessage {

    private final MessageHeader header;
    private final ByteBuffer messageBuffer;

    public NanoFIXMessage(ByteBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
        // every message has a header
        this.header = new MessageHeader();
    }

    @Override
    public MessageHeader header() {
        return header;
    }

    @Override
    public MsgType msgType() {
        return header.msgType();
    }

    @Override
    public long encode(final ByteBuffer buffer, final int offset) {
        int index = offset;
        int bodyLength = ByteBufferUtil.readableBytes(buffer);
        index += header.encode(buffer, index, bodyLength);

        buffer.put(messageBuffer);

        // update the checksum
        byte[] checksumBytes = ChecksumCalculator.calculateChecksum(buffer);
        addBytesField(Tags.CheckSum, checksumBytes);
        return -1;
    }

}
