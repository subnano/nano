package net.subnano.codec.json;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.util.ByteArrayUtil;

import java.nio.ByteBuffer;

/**
 * {@link JsonByteParser} is an extremely fast JSON parser that parses a stream of bytes and not characters.
 *
 * <p>This is by design and is not a limitation of the parser. As a result this parser should not be considered
 * as a replacement for the commonly available parsers.</p>
 *
 * <p>The parsing process is designed to allocate zero objects through the use of an index overlay parsing technique
 * and can be considered safe to use in a latency sensitive platform such as an HFT trading environment.</p>
 *
 * <p>No attempt is made to validate the source JSON. This parser merely iterates over the bytes within the buffer
 * and notifies the {@link JsonVisitor} as each byte is encountered. Schema validation can be added to the
 * {@link JsonVisitor} as the document is traversed.</p>
 *
 * TODO - ideally ByteBuffer should be a typed <T> source and buffer implementations should be subclasses
 *
 * @author Mark Wardell
 */
public class JsonByteParser {

    static final byte EOF = -1;
    static final String ERROR_INVALID_CHARACTER = "Invalid character in JSON source";
    static final String ERROR_UNEXPECTED_END = "Unexpected end of JSON source reached";
    static final String ERROR_UNQUOTED_TEXT = "Unquoted text encountered";

    static final byte[] TRUE_BYTES = ByteArrayUtil.asByteArray("true");
    static final byte[] FALSE_BYTES = ByteArrayUtil.asByteArray("false");

    static final char COMMA = ',';
    static final char COLON = ':';
    static final char QUOTE = '"';
    static final char OPEN_BRACE = '{';
    static final char CLOSE_BRACE = '}';
    static final char OPEN_ARRAY = '[';
    static final char CLOSE_ARRAY = ']';

    private ByteBuffer buffer;
    private int bufferIndex;

    public void parse(ByteBuffer buffer, JsonVisitor visitor) {
        this.buffer = buffer;
        this.bufferIndex = 0;
        while (hasNextByte()) {
            parseNext(visitor);
        }
    }

    private void parseNext(JsonVisitor visitor) {
        skipWhiteSpaces();
        byte nextByte = getByte();
        switch (nextByte) {
            case OPEN_BRACE:
                visitor.startObject(buffer, bufferIndex);
                nextByte();
                break;
            case CLOSE_BRACE:
                visitor.endObject(buffer, bufferIndex);
                nextByte();
                break;
            case OPEN_ARRAY:
                visitor.startArray(buffer, bufferIndex);
                nextByte();
                break;
            case CLOSE_ARRAY:
                visitor.endArray(buffer, bufferIndex);
                nextByte();
                break;
            case COMMA:
                visitor.comma(buffer, bufferIndex);
                nextByte();
                break;
            case COLON:
                visitor.colon(buffer, bufferIndex);
                nextByte();
                break;
            case QUOTE:
                parseString(visitor);
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                parseNumber(visitor);
                break;
            case 't':
                parseTrue(visitor);
                break;
            case 'f':
                parseFalse(visitor);
                break;
            case 'n':
                parseNull(visitor);
                break;
            default    :
                visitor.onError(buffer, bufferIndex, ERROR_INVALID_CHARACTER);
                nextByte();
        }
    }

    /**
     * Advance the buffer index by 1 and return the next byte in the buffer
     */
    private byte nextByte() {
        bufferIndex++;
        return getByte();
    }

    /**
     * Advance the buffer index by the specified number of bytes
     */
    private void skip(int bytes) {
        bufferIndex += bytes;
    }

    /**
     * Returns true if there are more available bytes in the buffer.
     * @return true When more bytes are available, otherwise false.
     */
    private boolean hasNextByte() {
        return bufferIndex < buffer.limit();
    }

    /**
     * Returns the next available byte in the buffer without altering the buffer index
     *
     * @return The next byte in the buffer or -1 if end of buffer reached
     */
    private byte getByte() {
        return hasNextByte() ? buffer.get(bufferIndex) : EOF;
    }

    private void skipWhiteSpaces() {
        boolean skipping = true;
        byte nextByte = getByte();
        while (skipping && hasNextByte()) {
            switch (nextByte) {
                case ' ':
                case '\n':
                case '\r':
                case '\t':
                    nextByte = nextByte();
                    break;

                default: {
                    skipping = false;
                }
            }
        }
    }

    private void parseString(JsonVisitor visitor) {
        // skip the opening quote
        byte nextByte = nextByte();
        int offset = bufferIndex;
        int len = 0;
        while (nextByte != '"' && hasNextByte()) {
            len++;
            nextByte = nextByte();
        }
        if (nextByte == '"') {
            // advance past the closing quote
            nextByte();
            visitor.onString(buffer, offset, len);
        }
        else
            visitor.onError(buffer, bufferIndex, ERROR_UNEXPECTED_END);
    }

    private void parseNumber(JsonVisitor visitor) {
        boolean numberComplete = false;
        // index at start of first digit
        int offset = bufferIndex;
        int len = 1;
        byte nextByte = nextByte();
        while (!numberComplete && hasNextByte()) {
            // TODO handle other cases such as negative, NaN and e notation
            switch (nextByte) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                    len++;
                    nextByte = nextByte();
                    break;

                default:
                    numberComplete = true;
            }
        }
        visitor.onNumber(buffer, offset, len);
    }

    private void parseTrue(JsonVisitor visitor) {
        int offset = bufferIndex;
        if (ByteBufferUtil.hasBytes(buffer, offset, TRUE_BYTES)) {
            visitor.onBoolean(buffer, offset, true);
            skip(TRUE_BYTES.length);
        }
        else {
            visitor.onError(buffer, offset, ERROR_UNQUOTED_TEXT);
            skipToNextDelimiter();
        }
    }

    private void parseFalse(JsonVisitor visitor) {
        int offset = bufferIndex;
        if (ByteBufferUtil.hasBytes(buffer, offset, FALSE_BYTES)) {
            visitor.onBoolean(buffer, offset, false);
            skip(FALSE_BYTES.length);
        }
        else {
            visitor.onError(buffer, offset, ERROR_UNQUOTED_TEXT);
            skipToNextDelimiter();
        }
    }

    private boolean parseNull(JsonVisitor visitor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void skipToNextDelimiter() {
        byte nextByte = nextByte();
        while (hasNextByte()
                && nextByte != COMMA
                && nextByte != COLON
                && nextByte != QUOTE
                && nextByte != CLOSE_BRACE) {
            nextByte = nextByte();
        }
    }

}
