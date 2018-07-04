package io.nano.core.lang;

/**
 * MutableInteger is a holder of an integer value that is not to be used when concurrency or atomicity are a concern.
 */

public class MutableInteger {

    private int value;

    public MutableInteger() {
    }

    public MutableInteger(int value) {
        set(value);
    }

    public void set(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MutableInteger) && (((MutableInteger) o).value == value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
