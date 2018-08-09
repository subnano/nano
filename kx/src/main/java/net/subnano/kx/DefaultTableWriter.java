package net.subnano.kx;

/**
 * Single responsibility and that is to insert records into kx table.
 * <p>
 * TODO handle different command to 'insert'
 */
public class DefaultTableWriter<T> implements KxTableWriter<T> {

    private final KxConnection kxConnection;
    private final String tableName;
    private final String command;
    private final KxEncoder<T> encoder;
    private final KxConnection.Mode mode;
    private final TableDataBuffer tableDataBuffer;

    DefaultTableWriter(final KxConnection connection,
                       final KxSchema kxSchema,
                       final KxEncoder<T> encoder,
                       final KxConnection.Mode mode) {
        this.kxConnection = connection;
        this.tableName = kxSchema.tableName();
        this.command = kxSchema.command();
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
            kxConnection.sync(tableName, command, tableDataBuffer.tableData());
        else
            kxConnection.async(tableName, command, tableDataBuffer.tableData());
    }

}
