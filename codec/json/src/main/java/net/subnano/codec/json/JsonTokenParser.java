package net.subnano.codec.json;

import java.nio.ByteBuffer;

/**
 * {@link JsonTokenParser} is an extremely fast Json parser that parses a stream of bytes and not characters.
 *
 * <p>This is not a limitation of the parser is it by design so in that respect this parser should not
 * be considered as a general purpose parser.</p>
 *
 * <p>The parsing process is designed to allocate zero objects through the use of an index overlay parsing technique
 * so can be considered safe to use in a latency sensitive platform such as an HFT trading environment.</p>
 *
 * @author Mark Wardell
 */
public class JsonTokenParser {

    private final ByteBuffer buffer;

    private int bufferIndex;
    private int dataPosition;
    private int tokenLength;

    public JsonTokenParser(ByteBuffer buffer) {
        this.buffer = buffer;
        this.bufferIndex = 0;
        this.dataPosition = 0;
        this.tokenLength = 0;
    }

    public boolean hasMoreTokens() {
        return bufferIndex < buffer.limit();   // -1 ???
    }

    byte nextByte() {
        return buffer.get(bufferIndex);
    }

    public void parseTokens(TokenVisitor visitor) {
        skipWhiteSpace();

        byte nextByte = nextByte();

        switch (nextByte) {
            case '{':
                visitor.startObject(buffer, bufferIndex);
                break;
            case '}':
                visitor.endObject(buffer, bufferIndex);
                break;
            case '[':
                visitor.startArray(buffer, bufferIndex);
                break;
            case ']':
                visitor.endArray(buffer, bufferIndex);
                break;
            case ',':
                visitor.comma(buffer, bufferIndex);
                break;
            case ':':
                visitor.colon(buffer, bufferIndex);
                break;
            case '"':
                parseStringToken(visitor);
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
                parseNumberToken(visitor);
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
            //default    :  { parseStringToken(); this.tokenBuffer.type[this.tokenIndex] = TokenTypes.JSON_STRING_TOKEN; }
        }
    }

    private void skipWhiteSpace() {
        boolean skipping = true;
        while (skipping) {
            switch (nextByte()) {
                case ' ':
                case '\n':
                case '\r':
                case '\t':
                    this.bufferIndex++;
                    break;

                default: {
                    skipping = false;
                }
            }
        }
    }

    private void parseStringToken(TokenVisitor visitor) {
        byte nextByte = nextByte();
        int offset = this.bufferIndex;
        int len = 0;
        while (nextByte != '"' && hasMoreTokens()) {
            len++;
            nextByte = nextByte();
        }
        visitor.onString(buffer, offset, len);
    }

    private void parseNumberToken(TokenVisitor visitor) {
        boolean tokenComplete = false;
        byte nextByte = nextByte();
        int offset = this.bufferIndex;
        int len = 0;
        while (!tokenComplete && hasMoreTokens()) {
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
                    break;

                default:
                    tokenComplete = true;
            }
        }
        visitor.onNumber(buffer, offset, len);
    }

    private boolean parseTrue(TokenVisitor visitor) {
        // TODO parseTrue
        return false;
    }

    private boolean parseFalse(TokenVisitor visitor) {
        // TODO parseFalse
        return false;
    }

    private boolean parseNull(TokenVisitor visitor) {
        // TODO parseNull
        return false;

    }

    public void nextToken() {
//        switch (this.buffer.type[this.bufferIndex]) {
//            case TokenTypes.JSON_STRING_TOKEN: {
//                this.dataPosition += this.buffer.length[this.bufferIndex] + 2;
//                break;
//            } // +2 because of the quotes
//            case TokenTypes.JSON_STRING_ENC_TOKEN: {
//                this.dataPosition += this.buffer.length[this.bufferIndex] + 2;
//                break;
//            } // +2 because of the quotes
//            case TokenTypes.JSON_CURLY_BRACKET_LEFT: {
//                this.dataPosition++;
//                break;
//            }
//            case TokenTypes.JSON_CURLY_BRACKET_RIGHT: {
//                this.dataPosition++;
//                break;
//            }
//            case TokenTypes.JSON_SQUARE_BRACKET_LEFT: {
//                this.dataPosition++;
//                break;
//            }
//            case TokenTypes.JSON_SQUARE_BRACKET_RIGHT: {
//                this.dataPosition++;
//                break;
//            }
//            case TokenTypes.JSON_COLON: {
//                this.dataPosition++;
//                break;
//            }
//            case TokenTypes.JSON_COMMA: {
//                this.dataPosition++;
//                break;
//            }
//            case TokenTypes.JSON_NULL_TOKEN: {
//                this.dataPosition += 4;
//                break;
//            }
//            default: {
//                this.dataPosition += this.tokenLength;
//            }
//        }
//        //this.dataPosition += this.tokenBuffer.length[this.tokenIndex]; //move data position to end of current token.
//        this.bufferIndex++;  //point to next token index array cell.
    }

}
