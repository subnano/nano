package net.nanofix.message;

import io.nano.core.util.ByteArrayUtil;
import io.nano.core.lang.ByteString;

import java.util.Objects;

public class MsgType extends ByteString {

    private final String value;
    private final String name;

    public MsgType(String value) {
        this(value, value);
    }

    public MsgType(String value, String name) {
        super(ByteArrayUtil.asByteArray(value));
        this.value = value;
        this.name = name;
    }

    public String value() {
        return value;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsgType that = (MsgType) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public String toString() {
        return name == null ? value : name + '(' + value + ')';
    }
}
