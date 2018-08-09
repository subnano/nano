package net.subnano.kx;

/**
 * @author Mark Wardell
 */
public interface KxWriterSource<T> {

    KxSchema schema();

    KxEncoder<T> encoder();

    KxConnection.Mode mode();

}
