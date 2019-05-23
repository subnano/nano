package net.subnano.codec.wire;

import static org.assertj.core.api.Assertions.assertThat;

final class EncoderTestHelper {

    static void verifyData(
            BufferReader bufferReader, int type, int offset, int len, Object expectedData) {
        switch (type) {
            case WireType.BYTE:
                assertThat(bufferReader.readByte(offset)).isEqualTo(expectedData);
                break;
            case WireType.SHORT:
                assertThat(bufferReader.readShort(offset)).isEqualTo(expectedData);
                break;
            case WireType.INT:
                assertThat(bufferReader.readInt(offset)).isEqualTo(expectedData);
                break;
            case WireType.LONG:
                assertThat(bufferReader.readLong(offset)).isEqualTo(expectedData);
                break;
            case WireType.DOUBLE:
                assertThat(bufferReader.readDouble(offset)).isEqualTo(expectedData);
                break;
            case WireType.STRING:
            case WireType.TEXT:
                assertThat(bufferReader.readString(offset, len)).isEqualTo(expectedData);
                break;
            default:
                throw new IllegalArgumentException("Unsupported wire type");
        }
    }
}
