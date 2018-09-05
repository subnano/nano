package net.subnano.codec.json;

import io.nano.core.buffer.AsciiBufferUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
class JsonTokenParserTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private JsonTokenParser parser;

    private TokenVisitor visitor = Mockito.spy(new ConsoleTokenVisitor());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parser = new JsonTokenParser(buffer);
    }

    @Test
    void hasMoreTokens() {
    }

    @Test
    void nextByte() {
    }

    @Test
    void parseTokens() {
        String json = "{\"result\": \"success\"}";
        prepareBuffer(buffer, json);
        parser.parseTokens(visitor);
    }

    private static void prepareBuffer(ByteBuffer buffer, String text) {
        buffer.clear();
        AsciiBufferUtil.putString(text, buffer);
        buffer.flip();
    }

    @Test
    void nextToken() {
    }

    @Test
    void tokenPosition() {
    }

    @Test
    void tokenLength() {
    }

    @Test
    void tokenType() {
    }
}