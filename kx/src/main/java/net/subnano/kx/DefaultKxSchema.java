package net.subnano.kx;

import kx.c;
import kx.c.Timespan;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mark Wardell
 */
public class DefaultKxSchema implements KxSchema {

    private final String tableName;
    private final String command;
    private final String[] columnNames;
    private final Object[] data;
    private final int batchSize;

    private DefaultKxSchema(Builder builder) {
        this.tableName = builder.tableName;
        this.command = builder.command;
        this.columnNames = builder.columnNames.toArray(new String[0]);
        this.data = builder.newTableData();
        this.batchSize = builder.batchSize;
    }

    @Override
    public String tableName() {
        return tableName;
    }

    @Override
    public String command() {
        return command;
    }

    @Override
    public String[] columnNames() {
        return columnNames;
    }

    @Override
    public Object[] data() {
        return data;
    }

    @Override
    public int batchSize() {
        return batchSize;
    }

    public static class Builder {

        private static final String DEFAULT_COMMAND = "insert";

        private String tableName;
        private String command = DEFAULT_COMMAND;
        private final List<String> columnNames = new ArrayList<>();
        private final List<ColumnType> columnTypes = new ArrayList<>();
        private int batchSize = 1;

        public Builder() {
            // nothing else to do
        }

        public Builder table(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder andCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder addColumn(String name, ColumnType columnType) {
            columnNames.add(name);
            columnTypes.add(columnType);
            return this;
        }

        public KxSchema build() {
            return new DefaultKxSchema(this);
        }

        private Object[] newTableData() {
            Object[] data = new Object[columnTypes.size()];
            for (int i = 0; i < columnTypes.size(); i++) {
                ColumnType type = columnTypes.get(i);
                switch (type) {
                    case Boolean:
                        data[i] = new boolean[batchSize];
                        break;
                    case Byte:
                        data[i] = new byte[batchSize];
                        break;
                    case Short:
                        data[i] = new short[batchSize];
                        break;
                    case Int:
                        data[i] = new int[batchSize];
                        break;
                    case Long:
                        data[i] = new long[batchSize];
                        break;
                    case Double:
                        data[i] = new double[batchSize];
                        break;
                    case Char:
                        data[i] = new char[batchSize];
                        break;
                    case String:
                        data[i] = new String[batchSize];
                        break;
                    case DateTime:
                        data[i] = new Date[batchSize];
                        break;
                    case Timestamp:
                        data[i] = new Timestamp[batchSize];
                        break;
                    case Timespan:
                        data[i] = new Timespan[batchSize];
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported column type: " + type);
                }
            }
            return data;
        }
    }

}
