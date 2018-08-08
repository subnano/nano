package io.subnano.kx;

/**
 * @author Mark Wardell
 */
class KxSampleWriterSource implements KxWriterSource<KxSample> {

    static String SAMPLE_TABLE_NAME = "sample";

    @Override
    public KxSchema schema() {
        return new DefaultKxSchema.Builder()
                .forTable(SAMPLE_TABLE_NAME)
                .addColumn("sym", ColumnType.String)
                .addColumn("age", ColumnType.Int)
                .addColumn("time", ColumnType.Timestamp)
                .build();
    }

    @Override
    public KxEncoder<KxSample> encoder() {
        return this::encode;
    }

    @Override
    public KxConnection.Mode mode() {
        return KxConnection.Mode.Async;
    }

    private void encode(KxSample source, TableDataBuffer buffer) {
        buffer.reset();
        buffer.addString(source.name);
        buffer.addInt(source.age);
        buffer.addTimestamp(source.time);
    }
}
