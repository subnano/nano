package io.subnano.kx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mark Wardell
 */
public class DefaultKxSchema implements KxSchema {

    private final String tableName;
    private final String[] columnNames;
    private final Object[] data;

    DefaultKxSchema(String tableName,
                    String[] columnNames,
                    Object[] data) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public String tableName() {
        return tableName;
    }

    @Override
    public String[] columnNames() {
        return columnNames;
    }

    @Override
    public Object[] data() {
        return data;
    }

    static class Builder {

        // Only supports single row for now
        private final int rowCount = 1;

        private String tableName;
        private final List<String> columnNames = new ArrayList<>();
        private final List<ColumnType> columnTypes = new ArrayList<>();

        public Builder() {
            // nothing else to do
        }

        public Builder forTable(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder addColumn(String name, ColumnType columnType) {
            columnNames.add(name);
            columnTypes.add(columnType);
            return this;
        }

        public KxSchema build() {
            return new DefaultKxSchema(
                    tableName,
                    columnNames.toArray(new String[0]),
                    newTableData()
            );
        }

        private Object[] newTableData() {
            Object[] data = new Object[columnTypes.size()];
            for (int i = 0; i < columnTypes.size(); i++) {
                ColumnType type = columnTypes.get(i);
                switch(type) {
                    case Boolean:
                        data[i] = new boolean[rowCount];
                        break;
                    case Byte:
                        data[i] = new byte[rowCount];
                        break;
                    case Short:
                        data[i] = new short[rowCount];
                        break;
                    case Int:
                        data[i] = new int[rowCount];
                        break;
                    case Long:
                        data[i] = new long[rowCount];
                        break;
                    case Float:
                        data[i] = new float[rowCount];
                        break;
                    case Double:
                        data[i] = new double[rowCount];
                        break;
                    case Char:
                        data[i] = new char[rowCount];
                        break;
                    case String:
                        data[i] = new String[rowCount];
                        break;
                    case Timestamp:
                        // TODO there must be a better way than Date ??
                        data[i] = new Date[rowCount];
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported column type: " + type);
                }
            }
            return data;
        }
    }

}
