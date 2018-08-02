package io.subnano.kx;

import java.io.IOException;

/**
 * Single responsibility and that is to insert records into kx table.
 * TODO handle different command to 'insert'
 * TODO handle error scenarios better
 */
public class DefaultTableWriter<T> implements KxTableWriter<T> {

    private static final String UPDATE_COMMAND = "insert";

    private final KxConnection kxConnection;
    private final String tableName;
    private final KxEncoder<T> encoder;
    private final KxConnection.Mode mode;
    private final TableDataBuffer tableDataBuffer;

    DefaultTableWriter(final KxConnection connection,
                       final String tableName,
                       final String[] columnNames,
                       final Object[] tableSchema,
                       final KxEncoder<T> encoder,
                       final KxConnection.Mode mode) {
        this.kxConnection = connection;
        this.tableName = tableName;
        this.encoder = encoder;
        this.mode = mode;
        this.tableDataBuffer = new TableDataBuffer(columnNames, tableSchema);
    }

    @Override
    public void write(T record) throws IOException {
        encoder.encoder(record, tableDataBuffer);
        // should store the method reference in the ctor to avoid a condition on every invocation
        // this is possibly optimized away anyway
        if (KxConnection.Mode.Sync == mode)
            kxConnection.sync(tableName, UPDATE_COMMAND, tableDataBuffer.flip());
        else
            kxConnection.async(tableName, UPDATE_COMMAND, tableDataBuffer.flip());
    }

}
