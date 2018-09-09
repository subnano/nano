package net.subnano.codec.json;

import io.nano.core.buffer.AsciiBufferUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.nio.ByteBuffer;

import static net.subnano.codec.json.JsonByteParser.ERROR_INVALID_CHARACTER;
import static net.subnano.codec.json.JsonByteParser.ERROR_UNQUOTED_TEXT;
import static org.mockito.Mockito.verify;

/**
 * TODO - add tests with invalid characters to exercise the parser
 * TODO - add tests with for negative numbers
 *
 * @author Mark Wardell
 */
class JsonByteParserTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private JsonByteParser parser;

    // enable logging while debugging
    private JsonVisitor visitor = Mockito.spy(new ConsoleJsonVisitor());

    // simply mock when debugging complete
    //private JsonVisitor visitor = Mockito.mock(JsonVisitor.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parser = new JsonByteParser();
    }

    @Test
    void parseAbsoluteMinimumJson() {
        prepareAndParseBuffer("{}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).endObject(buffer, 1);
    }

    @Test
    void parseJsonWithInvalidOrdering() {
        prepareAndParseBuffer(":}{");
        verify(visitor).colon(buffer, 0);
        verify(visitor).endObject(buffer, 1);
        verify(visitor).startObject(buffer, 2);
    }

    @Test
    void parseJsonWithInvalidCharacters() {
        prepareAndParseBuffer("{:@");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).colon(buffer, 1);
        verify(visitor).onError(buffer, 2, ERROR_INVALID_CHARACTER);
    }

    @Test
    void parseSingleString() {
        prepareAndParseBuffer("{\"name\"}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 4);
        verify(visitor).endObject(buffer, 7);
    }

    @Test
    void parseSingleNumber() {
        prepareAndParseBuffer("{123456}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onNumber(buffer, 1, 6);
        verify(visitor).endObject(buffer, 7);
    }

    @Test
    void parseMinimalStringAttribute() {
        prepareAndParseBuffer("{\"event\":\"price\"}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 5);
        verify(visitor).colon(buffer, 8);
        verify(visitor).onString(buffer, 10, 5);
        verify(visitor).endObject(buffer, 16);
    }

    @Test
    void parseMinimalNumericAttribute() {
        prepareAndParseBuffer("{\"num\":404}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 3);
        verify(visitor).colon(buffer, 6);
        verify(visitor).onNumber(buffer, 7, 3);
        verify(visitor).endObject(buffer, 10);
    }

    @Test
    void parseBooleanAttributeTrue() {
        prepareAndParseBuffer("{\"success\":true}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 7);
        verify(visitor).colon(buffer, 10);
        verify(visitor).onBoolean(buffer, 11, true);
        verify(visitor).endObject(buffer, 15);
    }

    @Test
    void parseBooleanAttributeFalse() {
        prepareAndParseBuffer("{\"success\":false}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 7);
        verify(visitor).colon(buffer, 10);
        verify(visitor).onBoolean(buffer, 11, false);
        verify(visitor).endObject(buffer, 16);
    }

    @Test
    void parseBooleanAttributeFalseFailed() {
        prepareAndParseBuffer("{\"success\":flase}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 7);
        verify(visitor).colon(buffer, 10);
        verify(visitor).onError(buffer, 11, ERROR_UNQUOTED_TEXT);
        verify(visitor).endObject(buffer, 16);
    }

    @Test
    void parseBothAttributes() {
        prepareAndParseBuffer("{\"result\":\"success\",\"code\":123}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 6);
        verify(visitor).colon(buffer, 9);
        verify(visitor).onString(buffer, 11, 7);
        verify(visitor).comma(buffer, 19);
        verify(visitor).onString(buffer, 21, 4);
        verify(visitor).colon(buffer, 26);
        verify(visitor).onNumber(buffer, 27, 3);
        verify(visitor).endObject(buffer, 30);
    }

    @Test
    void parseWithLiberalSpacing() {
        prepareAndParseBuffer("{ \"one\" :  \"yes_sir\" , \"two\" : 787989131  }");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 3, 3);
        verify(visitor).colon(buffer, 8);
        verify(visitor).onString(buffer, 12, 7);
        verify(visitor).comma(buffer, 21);
        verify(visitor).onString(buffer, 24, 3);
        verify(visitor).colon(buffer, 29);
        verify(visitor).onNumber(buffer, 31, 9);
        verify(visitor).endObject(buffer, 42);
    }

    @Test
    void parseArrays() {
        prepareAndParseBuffer("{\"result\":\"success\",\"bids\":[19, 18, 17], \"asks\":[21,22,23]}");
        verify(visitor).startObject(buffer, 0);
        verify(visitor).onString(buffer, 2, 6);
        verify(visitor).colon(buffer, 9);
        verify(visitor).onString(buffer, 11, 7);
        verify(visitor).comma(buffer, 19);
        // bids
        verify(visitor).onString(buffer, 21, 4);
        verify(visitor).colon(buffer, 26);
        verify(visitor).startArray(buffer, 27);
        verify(visitor).onNumber(buffer, 28, 2);
        verify(visitor).comma(buffer, 30);
        verify(visitor).onNumber(buffer, 32, 2);
        verify(visitor).comma(buffer, 34);
        verify(visitor).onNumber(buffer, 36, 2);
        verify(visitor).endArray(buffer, 38);
        verify(visitor).comma(buffer, 39);
        // asks
        verify(visitor).startArray(buffer, 48);
        // missed a few
        verify(visitor).endArray(buffer, 57);
        verify(visitor).endObject(buffer, 58);
    }

    private void prepareAndParseBuffer(String text) {
        System.out.println("Preparing: " + text);
        buffer.clear();
        AsciiBufferUtil.putCharSequence(text, buffer);
        buffer.flip();
        parser.parse(buffer, visitor);
    }
}