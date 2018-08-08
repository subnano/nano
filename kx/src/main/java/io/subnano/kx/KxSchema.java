package io.subnano.kx;

public interface KxSchema {

    String tableName();

    String command();

    String[] columnNames();

    Object[] data();

}
