package net.subnano.codec.json.sample;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import net.subnano.codec.json.sample.ImmutablePrice.ImmutableLevel;
import org.decimal4j.api.DecimalArithmetic;
import org.decimal4j.immutable.Decimal4f;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author Mark Wardell
 */
public class JacksonPriceCodec {

    private static final DecimalArithmetic ARITHMETIC = Decimal4f.DEFAULT_ARITHMETIC;

    private final ObjectMapper mapper = new ObjectMapper();

    public Price decode(ByteBuffer buffer) throws IOException {
        InputStream is = new ByteBufferBackedInputStream(buffer);
        JsonNode jsonNode = mapper.readTree(is);
        return createPrice(jsonNode);
    }

    private Price createPrice(JsonNode node) {
        return new ImmutablePrice(
                node.get("instrument").asText(),
                node.get("timestamp").asLong(),
                node.get("success").asBoolean(),
                createLevels(node.get("buy")),
                createLevels(node.get("sell"))
        );
    }

    private Level[] createLevels(JsonNode node) {
        Level[] levels = new ImmutableLevel[node.size()];
        int index = 0;
        for (JsonNode each : node) {
            levels[index++] = new ImmutableLevel(
                    ARITHMETIC.parse(each.get("quantity").asText()),
                    ARITHMETIC.parse(each.get("price").asText())
            );
        }
        return levels;
    }
}
