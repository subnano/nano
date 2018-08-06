package io.subnano.kx;

import kx.c.Flip;

/**
 * @author Mark Wardell
 */
public interface KxConnection {

    /**
     * Instructs the underlying library to connect to remote Kx server process.
     */
    void connect();

    /**
     * Returns true if socket is connected to remote Kx server process.
     *
     * @return true only if successfully connected
     */
    boolean isConnected();

    /**
     * Close the socket connection to the remote Kx server process
     */
    void close();

    /**
     * Creates a new instance of KxTableWriter for the given type.
     *
     * @param kxSchema the given KxSchema of the table
     * @param encoder  the encoder used to covert data from given type to the native Kx data
     * @param mode     the mode used to send message to kx process (can be Sync / Async)
     * @param <T>      type Java type representing the Kx table
     * @return a suitable KxTableWriter
     */
    <T> KxTableWriter<T> newTableWriter(KxSchema kxSchema, KxEncoder<T> encoder, Mode mode);

    /**
     * Invokes the command synchronously on the specified table with the corresponding Kx Flip data set
     *
     * @param table   the table to perform the operation on
     * @param command the command to execute
     * @param flip    the Kx Flip data used for the operation
     */
    void sync(String table, String command, Flip flip);

    /**
     * Invokes the command asynchronously on the specified table with the corresponding Kx Flip data set
     *
     * @param table   the table to perform the operation on
     * @param command the command to execute
     * @param flip    the Kx Flip data used for the operation
     */
    void async(String table, String command, kx.c.Flip flip);

    Object subscribe(String table);

    enum Mode {
        Sync,
        Async
    }
}
