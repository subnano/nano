package net.nanofix.message.util;

import io.nano.core.util.ByteArrayUtil;

import java.nio.ByteBuffer;

public final class ChecksumCalculator {

    private static final byte[][] CHECKSUMS = new byte[1000][3];

    private ChecksumCalculator() {
        // can't touch this
    }

    static {
        for (int i = 0; i < 256; i++) {
            CHECKSUMS[i] = ByteArrayUtil.as3ByteArray(i);
        }
    }

    public static int calculateChecksum(byte[] bytes, int offset, int len) {
        int checksum = 0;
        for (int index = offset; index < len; index++) {
            checksum += bytes[index];
        }
        return checksum % 256;
    }

    public static int calculateChecksum(ByteBuffer buffer, int offset, int len) {
        int checksum = 0;
        if (buffer != null) {
            for (int index = offset; index < len; index++) {
                checksum += buffer.get(index);
            }
        }
        return checksum % 256;
    }

    public static byte[] calculateChecksum(ByteBuffer[] buffers) {
        int checksum = 0;
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                if (buffer != null) {
                    for (int i = 0; i < buffer.position(); i++) {
                        checksum += buffer.get(i);
                    }
                }
            }
        }
        return CHECKSUMS[checksum % 256];
    }
}
