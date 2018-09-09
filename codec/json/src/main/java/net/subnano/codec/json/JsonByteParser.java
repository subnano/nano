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
 * TODO - it currently works directly with the underlying byte[] for performance - this wont work for direct buffers
 *
 * <b>Optimizations</b>
 * - converted buffer index from a member (uses load/store) to an argument which uses registers (9%)
 * - TODO true/false to test bytes locally and avoid method call
 * - TODO do we really need callbacks for colon & comma?
 *
 * @author Mark Wardell
 */
public class JsonByteParser {

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
    private byte[] bytes;
    private int limit;

    public void parse(ByteBuffer buffer, JsonVisitor visitor) {
        this.buffer = buffer;
        this.bytes = buffer.array();
        this.limit = buffer.limit();
        int index = 0;
        while (index < limit) {
            index = parseNext(index, visitor);
        }
    }

    private int parseNext(int index, JsonVisitor visitor) {
        byte nextByte = bytes[index];
        switch (nextByte) {
            case OPEN_BRACE:
                visitor.startObject(buffer, index);
                index++;
                break;
            case CLOSE_BRACE:
                visitor.endObject(buffer, index);
                index++;
                break;
            case OPEN_ARRAY:
                visitor.startArray(buffer, index);
                index++;
                break;
            case CLOSE_ARRAY:
                visitor.endArray(buffer, index);
                index++;
                break;
            case COMMA:
                visitor.comma(buffer, index);
                index++;
                break;
            case COLON:
                visitor.colon(buffer, index);
                index++;
                break;
            case QUOTE:
                index = parseString(index, visitor);
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
                index = parseNumber(index, visitor);
                break;
            case 't':
                index = parseTrue(index, visitor);
                break;
            case 'f':
                index = parseFalse(index, visitor);
                break;
            case 'n':
                index = parseNull(index, visitor);
                break;
            case ' ' :
            case '\n':
            case '\r':
            case '\t':
                index++;
                break;
            default:
                visitor.onError(buffer, index, ERROR_INVALID_CHARACTER);
                index++;
        }
        return index;
    }

    private int parseString(int index, JsonVisitor visitor) {
        // skip the opening quote
        byte nextByte = bytes[++index];
        int offset = index;
        while (nextByte != '"' && index < limit) {
            nextByte = bytes[++index];
        }
        if (nextByte == '"') {
            // advance past the closing quote
            index++;
            visitor.onString(buffer, offset, index - offset - 1);
        } else {
            visitor.onError(buffer, index, ERROR_UNEXPECTED_END);
        }
        return index;
    }

    private int parseNumber(int index, JsonVisitor visitor) {
        boolean numberComplete = false;
        // index at start of first digit
        int offset = index;
        byte nextByte = bytes[++index];
        while (!numberComplete && index < limit) {
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
                    nextByte = bytes[++index];
                    break;

                default:
                    numberComplete = true;
            }
        }
        visitor.onNumber(buffer, offset, index -offset);
        return index;
    }

    private int parseTrue(int index, JsonVisitor visitor) {
        int offset = index;
        if (ByteArrayUtil.equals(bytes, offset, TRUE_BYTES)) {
            visitor.onBoolean(buffer, offset, true);
            index += TRUE_BYTES.length;
        } else {
            visitor.onError(buffer, offset, ERROR_UNQUOTED_TEXT);
            index = skipToNextDelimiter(index);
        }
        return index;
    }

    private int parseFalse(int index, JsonVisitor visitor) {
        int offset = index;
        if (ByteArrayUtil.equals(bytes, offset, FALSE_BYTES)) {
            visitor.onBoolean(buffer, offset, false);
            index += FALSE_BYTES.length;
        } else {
            visitor.onError(buffer, offset, ERROR_UNQUOTED_TEXT);
            index = skipToNextDelimiter(index);
        }
        return index;
    }

    private int parseNull(int index, JsonVisitor visitor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private int skipToNextDelimiter(int index) {
        byte nextByte = bytes[++index];
        while (index < limit
                && nextByte != COMMA
                && nextByte != COLON
                && nextByte != QUOTE
                && nextByte != CLOSE_BRACE) {
            nextByte = bytes[++index];
        }
        return index;
    }

}
