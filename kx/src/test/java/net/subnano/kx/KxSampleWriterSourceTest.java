package net.subnano.kx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * We only write a test for this to ensure that the API is easily testable
 *
 * @author Mark Wardell
 */
public class KxSampleWriterSourceTest {

    private KxWriterSource<KxSample> kxWriterSource;
    private KxSample kxSample;

    @Mock
    private TableDataBuffer tableDataBuffer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        kxWriterSource = new KxSampleWriterSource();
        kxSample = new KxSample("Arthur Dent", 42, 13);
    }

    @Test
    void schema() {
        KxSchema schema = kxWriterSource.schema();
        assertThat(schema.tableName()).isEqualTo("sample");
        assertThat(schema.columnNames()).isEqualTo(new String[]{"sym", "age", "time"});
    }

    @Test
    void encoder() {
        KxEncoder<KxSample> encoder = kxWriterSource.encoder();
        encoder.encode(kxSample, tableDataBuffer);
        verify(tableDataBuffer).reset();
        verify(tableDataBuffer).addString("Arthur Dent");
        verify(tableDataBuffer).addInt(42);
        verify(tableDataBuffer).addTimestamp(13);
    }

    @Test
    void mode() {
        assertThat(kxWriterSource.mode()).isEqualTo(KxConnection.Mode.Async);
    }
}