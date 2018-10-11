package net.subnano.codec.wire;

public interface Decoder<T> {

    void decode(T source);

}
