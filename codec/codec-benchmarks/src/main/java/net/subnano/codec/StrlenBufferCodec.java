package net.subnano.codec;

import java.nio.ByteBuffer;

/**
 * Doesn't actually require zero byte check to determine length but does iterate over every byte.
 *
 * Primary purpose is for comparison against other codec - the one concern is that all code is optimized away.
 *
 * @author Mark Wardell
 */
public class StrlenBufferCodec {

    public byte decode(ByteBuffer buffer) {
        int pos = 0;
        int len = buffer.remaining();
        byte nextByte = 0;
        while (pos < len) {
            nextByte = buffer.get(pos++);
        }
        return nextByte;
    }

}
