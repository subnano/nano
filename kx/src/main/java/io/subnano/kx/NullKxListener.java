package io.subnano.kx;

/**
 * @author Mark Wardell
 */
public class NullKxListener implements KxListener {

    @Override
    public void onStateUpdated(ConnectState state) {
        // NO-OP
    }

    @Override
    public void onError(Throwable cause) {
        // NO-OP
    }

    @Override
    public void onMessage(Object message) {
        // NO-OP
    }
}
