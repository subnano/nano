package io.nano.core.pool;

import java.util.function.Supplier;

public class SimplePool<T> implements Pool<T> {

    private final Supplier<T> supplier;

    public SimplePool(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T obtain() {
        return null;
    }

    @Override
    public void release(T instance) {

    }
}
