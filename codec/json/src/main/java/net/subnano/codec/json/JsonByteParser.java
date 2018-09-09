package net.subnano.codec.json;

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
 * TODO - it currently works directly with the underlying byt[] for performance - this wont work for direct buffers
 *
 * <b>Possible Optimizations</b>
 *
 * - convert bufferIndex from a member (uses load/store) to an argument which would use registers
 * - true/false to test bytes locally and avoid method call
 * - do we really need callbacks for colon & comma?
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
    private byte[] bytes;
    private int limit;

    public void parse(ByteBuffer buffer, JsonVisitor visitor) {
        this.buffer = buffer;
        this.bytes = buffer.array();
        this.limit = buffer.limit();
        this.bufferIndex = 0;
        while (bufferIndex < limit) {
            parseNext(visitor);
        }
    }

    private void parseNext(JsonVisitor visitor) {
        byte nextByte = bytes[bufferIndex];
        switch (nextByte) {
            case OPEN_BRACE:
                visitor.startObject(buffer, bufferIndex);
                bufferIndex++;
                break;
            case CLOSE_BRACE:
                visitor.endObject(buffer, bufferIndex);
                bufferIndex++;
                break;
            case OPEN_ARRAY:
                visitor.startArray(buffer, bufferIndex);
                bufferIndex++;
                break;
            case CLOSE_ARRAY:
                visitor.endArray(buffer, bufferIndex);
                bufferIndex++;
                break;
            case COMMA:
                visitor.comma(buffer, bufferIndex);
                bufferIndex++;
                break;
            case COLON:
                visitor.colon(buffer, bufferIndex);
                bufferIndex++;
                break;
            case QUOTE:
                parseString(visitor);
                break;
            case '-':
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
            case ' ' :
            case '\n':
            case '\r':
            case '\t':
                bufferIndex++;
                break;
            default:
                visitor.onError(buffer, bufferIndex, ERROR_INVALID_CHARACTER);
                bufferIndex++;
        }
    }

    private void parseString(JsonVisitor visitor) {
        // skip the opening quote
        byte nextByte = bytes[++bufferIndex];
        int offset = bufferIndex;
        while (nextByte != '"' && bufferIndex < limit) {
            nextByte = bytes[++bufferIndex];
        }
        if (nextByte == '"') {
            // advance past the closing quote
            bufferIndex++;
            visitor.onString(buffer, offset, bufferIndex - offset - 1);
        } else {
            visitor.onError(buffer, bufferIndex, ERROR_UNEXPECTED_END);
        }
    }

    private void parseNumber(JsonVisitor visitor) {
        boolean numberComplete = false;
        // index at start of first digit
        int offset = bufferIndex;
        byte nextByte = bytes[++bufferIndex];
        while (!numberComplete && bufferIndex < limit) {
            // TODO handle other cases such as NaN and e notation
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
                    nextByte = bytes[++bufferIndex];
                    break;

                default:
                    numberComplete = true;
            }
        }
        visitor.onNumber(buffer, offset, bufferIndex-offset);
    }

    private void parseTrue(JsonVisitor visitor) {
        int offset = bufferIndex;
        if (ByteArrayUtil.equals(bytes, offset, TRUE_BYTES)) {
            visitor.onBoolean(buffer, offset, true);
            bufferIndex += TRUE_BYTES.length;
        } else {
            visitor.onError(buffer, offset, ERROR_UNQUOTED_TEXT);
            skipToNextDelimiter();
        }
    }

    private void parseFalse(JsonVisitor visitor) {
        int offset = bufferIndex;
        if (ByteArrayUtil.equals(bytes, offset, FALSE_BYTES)) {
            visitor.onBoolean(buffer, offset, false);
            bufferIndex += FALSE_BYTES.length;
        } else {
            visitor.onError(buffer, offset, ERROR_UNQUOTED_TEXT);
            skipToNextDelimiter();
        }
    }

    private boolean parseNull(JsonVisitor visitor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void skipToNextDelimiter() {
        byte nextByte = bytes[++bufferIndex];
        while (bufferIndex < limit
                && nextByte != COMMA
                && nextByte != COLON
                && nextByte != QUOTE
                && nextByte != CLOSE_BRACE) {
            nextByte = bytes[++bufferIndex];
        }
    }

}
