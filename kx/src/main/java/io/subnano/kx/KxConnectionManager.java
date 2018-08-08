package io.subnano.kx;

import kx.c.Flip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for reconnection to a Kx system when the socket connection is lost.
 * <p>
 * TODO add connection retry logic
 * TODO add support for multiple Kx server processes for fault tolerance.
 * TODO create a builder to create a custom connection manager with required properties
 *
 * @author Mark Wardell
 */
public class KxConnectionManager implements KxConnection, KxListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KxConnectionManager.class);

    private static final int DEFAULT_RECONNECT_INTERVAL_MS = 5_000;

    private final DefaultKxConnection connection;
    private final int reconnectInterval;
    private final KxListener kxListener;

    private int connectionAttempt = 0;

    private ConnectState state = ConnectState.Closed;

    public KxConnectionManager(String host, int port, KxListener kxListener) {
        this(host, port, DEFAULT_RECONNECT_INTERVAL_MS, kxListener);
    }

    public KxConnectionManager(String host, int port, int reconnectInterval, KxListener kxListener) {
        this.connection = new DefaultKxConnection(host, port, this);
        this.reconnectInterval = reconnectInterval;
        this.kxListener = kxListener;
    }

    @Override
    public void connect() {
        setConnectState(ConnectState.Connecting);
        tryConnect(++connectionAttempt);
    }

    private void tryConnect(int connectionAttempt) {
        if (connectionAttempt > 1) {
            try {
                LOGGER.info("Sleeping {} ms before attempting to reconnect", reconnectInterval);
                Thread.sleep(reconnectInterval);
                LOGGER.info("Connection attempt # {}", connectionAttempt);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        connection.connect();
    }

    @Override
    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void close() {
        connection.close();
    }

    @Override
    public <T> KxTableWriter<T> newTableWriter(KxWriterSource<T> kxWriterSource) {
        // TODO we could around this code dupe if we extended DefaultKxConnection
        // originally had a problem with that - need to investigate again further
        return new DefaultTableWriter<>(
                this,
                kxWriterSource.schema(),
                kxWriterSource.encoder(),
                kxWriterSource.mode()
        );
    }

    @Override
    public void sync(String table, String command, Flip flip) {
        connection.sync(table, command, flip);
    }

    @Override
    public void async(String table, String command, Flip flip) {
        connection.async(table, command, flip);
    }

    @Override
    public Object subscribe(String table) {
        return connection.subscribe(table);
    }

    @Override
    public void onStateUpdated(ConnectState state) {
        setConnectState(state);
    }

    @Override
    public void onError(Throwable cause) {
        // Connect failed - need to try reconnecting
        if (isConnectState(ConnectState.Connecting)) {
            tryConnect(++connectionAttempt);
        }
        // ignore if we were closing the connection
        else if (isConnectState(ConnectState.Closing)) {
            if (connection.isConnected()) {
                LOGGER.warn("Error closing connection - still connected!");
                // TODO do we need to retry closing connection?
            }
            // error closing but managed to close anyway
            else {
                setConnectState(ConnectState.Closed);
            }
        }
        else if (isConnectState(ConnectState.Connected)) {
            kxListener.onError(cause);

            setConnectState(ConnectState.Connecting);
            connectionAttempt = 0;
            tryConnect(++connectionAttempt);
        }
    }

    @Override
    public void onMessage(Object message) {
        kxListener.onMessage(message);
    }

    private boolean isConnectState(ConnectState state) {
        return this.state == state;
    }

    private void setConnectState(ConnectState state) {
        this.state = state;
        kxListener.onStateUpdated(state);
    }

}
