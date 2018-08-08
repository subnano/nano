package io.subnano.kx;

/**
 * Single responsibility and that is to insert records into kx table.
 * <p>
 * TODO handle different command to 'insert'
 */
public class DefaultTableWriter<T> implements KxTableWriter<T> {

    private static final String UPDATE_COMMAND = "insert";

    private final KxConnection kxConnection;
    private final String tableName;
    private final KxEncoder<T> encoder;
    private final KxConnection.Mode mode;
    private final TableDataBuffer tableDataBuffer;

    DefaultTableWriter(final KxConnection connection,
                       final KxSchema kxSchema,
                       final KxEncoder<T> encoder,
                       final KxConnection.Mode mode) {
        this.kxConnection = connection;
        this.tableName = kxSchema.tableName();
        this.encoder = encoder;
        this.mode = mode;
        this.tableDataBuffer = new TableDataBuffer(
                kxSchema.columnNames(),
                kxSchema.data()
        );
    }

    @Override
    public void write(T record) {
        encoder.encode(record, tableDataBuffer);
        // should store the method reference in the ctor to avoid a condition on every invocation
        // this is possibly optimized away anyway
        if (KxConnection.Mode.Sync == mode)
            kxConnection.sync(tableName, UPDATE_COMMAND, tableDataBuffer.flip());
        else
            kxConnection.async(tableName, UPDATE_COMMAND, tableDataBuffer.flip());
    }

}
