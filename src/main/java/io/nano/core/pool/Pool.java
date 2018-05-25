package io.nano.core.pool;

public interface Pool<T> {
    T obtain();
    void release(T instance);
}
