package io.subnano.kx;


import io.subnano.kx.KxConnection.Mode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class KxConnectionTest {

    private static String SAMPLE_TABLE_NAME = "sample";

    private DefaultKxConnection connection;

    @Mock
    private KxListener kxListener;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        connection = new DefaultKxConnection("localhost", 5001, null, null, kxListener);
        connection.connect();
    }

    @AfterEach
    void tearDown() {
        connection.close();
    }

    @Test
    void writeSingleRow() throws Exception {
        KxTableWriter<KxSample> tableWriter = new KxSampleWriter(connection);
        KxSample sample = new KxSample();
        sample.name = "Arthur Dent";
        sample.age = 42;
        tableWriter.write(sample);
    }


    @Test
    void queryTable() throws Exception {
        KxTableWriter<KxSample> tableWriter = new KxSampleWriter(connection);
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
        MutableGcEvent event = getGcEvent();
        for (int i = 0; i < 10; i++) {
            event.timestamp(System.currentTimeMillis());
            tableWriter.write(event);
            Thread.sleep(5_000);
        }
    }
*/

    class KxSampleWriter implements KxTableWriter<KxSample> {

        private final KxTableWriter<KxSample> tableWriter;

        KxSampleWriter(DefaultKxConnection connection) {
            this.tableWriter = connection.newTableWriter(buildKxSchema(), this::update, Mode.Async);
        }

        @Override
        public void write(KxSample record) throws IOException {
            tableWriter.write(record);
        }

        private KxSchema buildKxSchema() {
            return new DefaultKxSchema.Builder()
                    .forTable(SAMPLE_TABLE_NAME)
                    .addColumn("name", ColumnType.String)
                    .addColumn("age", ColumnType.Int)
                    .build();
        }

        private void update(KxSample event, TableDataBuffer buffer) {
            buffer.reset();
            buffer.addString(event.name);
            buffer.addInt(event.age);
        }
    }

    /**
     * @author Mark Wardell
     */
    static class KxSample {

        String name;
        int age;

    }
}