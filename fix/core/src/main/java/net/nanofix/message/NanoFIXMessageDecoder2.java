package net.nanofix.message;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.util.ByteArrayUtil;
import net.nanofix.message.util.ChecksumCalculator;

import java.nio.ByteBuffer;

import static io.nano.core.buffer.ByteBufferUtil.NOT_FOUND_INDEX;
import static net.nanofix.util.FIXBytes.EQUALS;
import static net.nanofix.util.FIXBytes.SOH;

/**
 * The first three fields are mandatory and must appear in this order:
 * <p>
 * BeginString (8) - Identifies the beginning of a FIX message. E.g. 8=FIX.4.4.
 * <p>
 * BodyLength (9) - The number of bytes in the message following the BodyLength (9) field up to, and including,
 * the delimiter immediately preceding the CheckSum (10) field.
 * <p>
 * MsgType (35) - Defines message type. E.g. 35=A.
 * <p>
 * CheckSum (10) - Always the last field and the value always contains 3 bytes. E.g. 10=093.
 * Calculated as modulo 256 of the sum of every byte in the message up to but not including the checksum field itself.
 * <p>
 * TODO body length value validation (all integer)
 * TODO only read as far as body length + checksum
 * TODO complain if checksum is missing
 * <p>
 * @author Mark Wardell
 */
public class NanoFIXMessageDecoder2 implements FIXMessageDecoder2 {

    private static final String EQUAL_NOT_FOUND_ERROR_MESSAGE = "Tag value delimiter '=' not found after index";
    private static final String SOH_NOT_FOUND_ERROR_MESSAGE = "Field delimiter 'SOH' not found after index";
    private static final String BEGIN_STRING_ERROR_MESSAGE = "Message must start with with the correct begin string 8=FIX.";
    private static final String BODY_LEN_SECOND_FIELD_ERROR_MESSAGE = "BodyLength(9) must be the second field in the message";
    private static final String MSG_TYPE_THIRD_FIELD_ERROR_MESSAGE = "MsgType(35) must be the third field in the message";
    private static final String BODY_LEN_INCORRECT_ERROR_MESSAGE = "BodyLength(9) value is incorrect";
    private static final String BODY_LEN_INVALID_ERROR_MESSAGE = "BodyLength(9) value is invalid";
    private static final String CHECKSUM_INCORRECT_ERROR_MESSAGE = "Invalid checksum!";

    private static final int MIN_BODY_LEN = 5; // 8=FIX.4.x|9=NN|35=X|10=nnn|
    private static final int MAX_BODY_LEN = 1024 * 1024;

    private final byte[] tagBytes = new byte[16];

    @Override
    public void decode(ByteBuffer buffer, FIXMessageVisitor2 visitor) {
        // TODO different implementations for different ByteBuffer implementations
        if (buffer.hasArray())
            decodeByteArray(buffer, visitor, buffer.position());
        else
            decodeDirectBuffer(buffer, visitor);
    }

    private void decodeByteArray(ByteBuffer buffer, FIXMessageVisitor2 visitor, int endIndex) {
        byte[] bytes = buffer.array();
        // start at current position?
        int initialOffset = 0;

        // initialise a few counters
        int bodyLen = 0;
        int bodyStartIndex = 0;
        int tagIndex = initialOffset;
        int tagCount = 0;
        while (tagIndex < endIndex) {
            int equalIndex = ByteArrayUtil.indexOf(bytes, tagIndex, EQUALS);
            if (equalIndex == NOT_FOUND_INDEX) {
                visitor.onError(tagIndex, EQUAL_NOT_FOUND_ERROR_MESSAGE);
                break;
            }

            int valueIndex = equalIndex + 1;
            int sohIndex = ByteArrayUtil.indexOf(bytes, valueIndex, SOH);
            if (sohIndex == NOT_FOUND_INDEX) {
                visitor.onError(valueIndex, SOH_NOT_FOUND_ERROR_MESSAGE);
                break;
            }

            int tag = AsciiBufferUtil.getInt(bytes, tagIndex, equalIndex - tagIndex);
            int valueLen = sohIndex - valueIndex;

            // check first three tags are correct
            if (tagCount <= 2) {

                // check first tag is the FIX BeginString
                if (tagCount == 0) {
                    if (tag != Tags.BeginString) {
                        visitor.onError(tagIndex, BEGIN_STRING_ERROR_MESSAGE);
                        break;
                    }
                }
                // check second tag is MsgBody
                else if (tagCount == 1) {
                    if (tag != Tags.BodyLength) {
                        visitor.onError(tagIndex, BODY_LEN_SECOND_FIELD_ERROR_MESSAGE);
                        break;
                    }
                    if (valueLen > 4) {
                        visitor.onError(tagIndex, BODY_LEN_INVALID_ERROR_MESSAGE);
                        break;
                    }
                    bodyLen = AsciiBufferUtil.getInt(bytes, valueIndex, valueLen);

                    if (bodyLen < MIN_BODY_LEN || bodyLen > MAX_BODY_LEN) {
                        visitor.onError(tagIndex, BODY_LEN_INVALID_ERROR_MESSAGE);
                        break;
                    }

                    // return and wait for more data
                    // TODO need to account for bytes already read + test
                    if (bodyLen > bytes.length) {
                        break;
                    }

                    // keep a record of when the body starts
                    bodyStartIndex = sohIndex + 1;
                }

                // check third tag is MsgType
                else if (tagCount == 2) {
                    if (tag != Tags.MsgType) {
                        visitor.onError(tagIndex, MSG_TYPE_THIRD_FIELD_ERROR_MESSAGE);
                        break;
                    }
                }
            }

            // last consistency check for checksum field
            else if (tag == Tags.CheckSum) {
                int actualBodyLength = tagIndex - bodyStartIndex;
                if (bodyLen != actualBodyLength) {
                    visitor.onError(tagIndex, BODY_LEN_INCORRECT_ERROR_MESSAGE);
                }
                // check that checksum value is correct
                int checksum = AsciiBufferUtil.getInt(bytes, valueIndex, valueLen);
                int calculatedChecksum = ChecksumCalculator.calculateChecksum(
                        bytes, initialOffset, tagIndex - initialOffset);

                if (checksum != calculatedChecksum) {
                    visitor.onError(tagIndex, CHECKSUM_INCORRECT_ERROR_MESSAGE);
                }
            }

            // notify visitor of next tag value pair
            visitor.onTag(buffer, tag, valueIndex, valueLen);

            // move offset to next available byte
            tagIndex = sohIndex + 1;

            // increment the tag counter
            tagCount++;
        }
    }

    private void decodeDirectBuffer(ByteBuffer buffer, FIXMessageVisitor2 visitor) {
        // start at current position?
        int initialOffset = 0;

        // initialise a few counters
        int bodyLen = 0;
        int bodyStartIndex = 0;
        int tagIndex = initialOffset;
        int tagCount = 0;
        int endIndex = buffer.position();
        while (tagIndex < endIndex) {
            int equalIndex = ByteBufferUtil.indexOf(buffer, tagIndex, EQUALS);
            if (equalIndex == NOT_FOUND_INDEX) {
                visitor.onError(tagIndex, EQUAL_NOT_FOUND_ERROR_MESSAGE);
                break;
            }
            int tagLen = equalIndex - tagIndex;
            int valueIndex = equalIndex + 1;

            // Read the tag bytes into our array before we create the integer
            buffer.get(tagBytes, tagIndex, tagLen);
            int tag = AsciiBufferUtil.getInt(tagBytes, 0, tagLen);

            int sohIndex = ByteBufferUtil.indexOf(buffer, valueIndex, SOH);
            if (sohIndex == NOT_FOUND_INDEX) {
                visitor.onError(valueIndex, SOH_NOT_FOUND_ERROR_MESSAGE);
                break;
            }
            int valueLen = sohIndex - valueIndex;

            // check first tag is the FIX BeginString
            if (tagCount == 0) {
                if (tag != Tags.BeginString) {
                    visitor.onError(tagIndex, BEGIN_STRING_ERROR_MESSAGE);
                    break;
                }
            }
            // check MsgBody
            else if (tagCount == 1) {
                if (tag != Tags.BodyLength) {
                    visitor.onError(tagIndex, BODY_LEN_SECOND_FIELD_ERROR_MESSAGE);
                    break;
                }
                if (valueLen > 4) {
                    visitor.onError(tagIndex, BODY_LEN_INVALID_ERROR_MESSAGE);
                    break;
                }
                bodyLen = AsciiBufferUtil.getInt(buffer, valueIndex, valueLen);

                if (bodyLen < MIN_BODY_LEN || bodyLen > MAX_BODY_LEN) {
                    visitor.onError(tagIndex, BODY_LEN_INVALID_ERROR_MESSAGE);
                    break;
                }

                // return and wait for more data
                if (bodyLen > buffer.limit()) {
                    break;
                }
            }

            // check MsgType is the third field
            else if (tagCount == 2) {
                if (tag != Tags.MsgType) {
                    visitor.onError(tagIndex, MSG_TYPE_THIRD_FIELD_ERROR_MESSAGE);
                    break;
                }
            }

            // last consistency check for checksum field
            else if (tag == Tags.CheckSum) {
                int actualBodyLength = tagIndex - bodyStartIndex;
                if (bodyLen != actualBodyLength) {
                    visitor.onError(tagIndex, BODY_LEN_INCORRECT_ERROR_MESSAGE);
                }
                // check that checksum value is correct
                int checksum = AsciiBufferUtil.getInt(buffer, valueIndex, valueLen);
                int calculatedChecksum = ChecksumCalculator.calculateChecksum(
                        buffer, initialOffset, tagIndex - initialOffset);

                if (checksum != calculatedChecksum) {
                    //System.out.println("checksum:" + checksum + " calculatedChecksum: " + calculatedChecksum);
                    visitor.onError(tagIndex, CHECKSUM_INCORRECT_ERROR_MESSAGE);
                }
            }

            // notify visitor of next tag value pair
            visitor.onTag(buffer, tag, valueIndex, valueLen);

            // move offset to next available byte
            tagIndex += (tagLen + valueLen + 2);

            // keep a record of when the body starts
            if (tagCount == 1) {
                bodyStartIndex = tagIndex;
            }

            // increment the tag counter
            tagCount++;

            // ask the visitor if message iteration should continue
            if (visitor.complete()) {
                break;
            }
        }
    }

}
