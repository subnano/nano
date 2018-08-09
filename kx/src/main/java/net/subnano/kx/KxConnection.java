package net.subnano.kx;

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
     * @param kxWriterSource the given source of the KxWriter details
     * @param <T>            type Java type representing the Kx table
     * @return a suitable KxTableWriter for the given Java type
     */
    <T> KxTableWriter<T> newTableWriter(KxWriterSource<T> kxWriterSource);

    /**
     * Invokes the command synchronously on the specified table with the corresponding Kx Flip data set
     *
     * @param table     the table to perform the operation on
     * @param command   the command to execute
     * @param data      the data used for the operation
     */
    void sync(String table, String command, Object data);

    /**
     * Invokes the command asynchronously on the specified table with the corresponding Kx Flip data set
     *
     * @param table   the table to perform the operation on
     * @param command the command to execute
     * @param data    the data used for the operation
     */
    void async(String table, String command, Object data);

    Object subscribe(String table);

    enum Mode {
        Sync,
        Async
    }
}
