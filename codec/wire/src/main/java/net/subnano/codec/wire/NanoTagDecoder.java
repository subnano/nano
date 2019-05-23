package net.subnano.codec.wire;

import static net.subnano.codec.wire.WireType.BYTE_SIZE;
import static net.subnano.codec.wire.WireType.SHORT_SIZE;

/**
 * TODO read buffer header
 * TODO read tag, type and call
 */
public class NanoTagDecoder {

    public void decode(BufferReader bufferReader, TagVisitor visitor) {
        // TODO read buffer header
        int offset = 0;
        int len;
        while (offset <= bufferReader.position()) {
            int packed = bufferReader.readShort(offset);
            int tag = WireUtil.decodeTag(packed);
            byte type = WireUtil.decodeType(packed);
            offset += SHORT_SIZE;

            switch (type) {
                case WireType.BYTE:
                case WireType.SHORT:
                case WireType.INT:
                case WireType.LONG:
                case WireType.DOUBLE:
                    int valueLen = WireType.sizeOf(type);
                    visitor.onTag(bufferReader, tag, type, offset, valueLen);
                    offset += valueLen;
                    break;
                case WireType.STRING:
                    len = bufferReader.readByte(offset) & 0xff;
                    visitor.onTag(bufferReader, tag, type, offset + 1, len);
                    offset += (BYTE_SIZE + len);
                    break;
                case WireType.TEXT:
                    len = bufferReader.readShort(offset) & 0xffff;
                    visitor.onTag(bufferReader, tag, type, offset + 2, len);
                    offset += (SHORT_SIZE + len);
                    break;
                default:
            }
        }
    }

    public boolean hasNext() {
        return true;
    }

    public void next(TagVisitor vistor) {

    }
}
