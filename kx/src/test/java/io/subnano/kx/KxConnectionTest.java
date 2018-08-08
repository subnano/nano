package io.subnano.kx;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.subnano.kx.KxSampleWriterSource.SAMPLE_TABLE_NAME;

class KxConnectionTest {

    private DefaultKxConnection connection;

    @Mock
    private KxListener kxListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        connection = new DefaultKxConnection("localhost", 5001, kxListener);
        connection.connect();
    }

    @AfterEach
    void tearDown() {
        connection.close();
    }

    @Test
    void writeSingleRow() {
        KxTableWriter<KxSample> tableWriter = connection.newTableWriter(new KxSampleWriterSource());
        KxSample sample = new KxSample("Arthur Dent", 42, -1);
        tableWriter.write(sample);
    }


    @Test
    void queryTable()  {
        KxTableWriter<KxSample> tableWriter = connection.newTableWriter(new KxSampleWriterSource());
        // Run sub function and store result
        //Object[] response = (Object[]) qConnection.k(".u.sub[`trade;`]");
        connection.subscribe(SAMPLE_TABLE_NAME);
        KxSample sample = new KxSample();
        sample.name = "Arthur Dent";
        sample.age = 42;
        tableWriter.write(sample);
    }

/*
    @Test
    void writeMultipleRows() throws Exception {
        KxTableWriter<GcEvent> tableWriter = new GcEventWriter(connection);
x        MutableGcEvent event = getGcEvent();
        for (int i = 0; i < 10; i++) {
            event.timestamp(System.currentTimeMillis());
            tableWriter.write(event);
            Thread.sleep(5_000);
        }
    }
*/

}