package io.subnano.kx;

/**
 * @author Mark Wardell
 */
public interface KxListener {

    void onConnect();

    void onClose();

    void onError(Throwable cause);

    void onMessage(Object message);

}
