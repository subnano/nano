package net.subnano.kx;

public interface KxSchema {

    /**
     * Returns the table name for this schema definition
     * @return table name
     */
    String tableName();

    /**
     * Returns the command used for updating the table
     * @return command name
     */
    String command();

    /**
     * Returns the names of the columns in the table
     * @return array of strings
     */
    String[] columnNames();

    /**
     * Returns the low level object array used to write data to the table
     * @return object array
     */
    Object[] data();

    /**
     * Returns the batch size used when writing data to the table
     * @return positive integer
     */
    int batchSize();
}
