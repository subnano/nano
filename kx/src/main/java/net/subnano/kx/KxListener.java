package net.subnano.kx;

/**
 * @author Mark Wardell
 */
public interface KxListener {

    void onStateUpdated(ConnectState state);

    void onError(Throwable cause);

    void onMessage(Object message);

}
