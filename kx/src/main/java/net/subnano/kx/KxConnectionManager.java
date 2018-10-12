package net.subnano.kx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is responsible for reconnection to a Kx system when the socket connection is lost.
 * <p>
 * TODO need to inject an Executor to handle reconnection attempts (easier to test then too)
 * TODO add support for multiple Kx server processes for fault tolerance.
 * TODO create a builder to create a custom connection manager with required properties
 *
 * @author Mark Wardell
 */
public class KxConnectionManager implements KxConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(KxConnectionManager.class);

    private static final int DEFAULT_RECONNECT_INTERVAL_MS = 5_000;

    private final KxConnection connection;
    private final int reconnectInterval;
    private final KxListener kxListener;

    private int connectionAttempt = 0;

    private AtomicReference<ConnectState> connectState = new AtomicReference<>(ConnectState.Closed);

    public KxConnectionManager(String host, int port, KxListener kxListener) {
        this(host, port, DEFAULT_RECONNECT_INTERVAL_MS, kxListener);
    }

    public KxConnectionManager(String host, int port, int reconnectInterval, KxListener kxListener) {
        this.connection = new DefaultKxConnection(host, port, new LocalKxListener());
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
    public void sync(String table, String command, Object data) {
        connection.sync(table, command, data);
    }

    @Override
    public void async(String table, String command, Object data) {
        connection.async(table, command, data);
    }

    @Override
    public KxTableReader subscribe(String table) {
        return connection.subscribe(table);
    }

    private boolean isConnectState(ConnectState state) {
        return connectState.get() == state;
    }

    private void setConnectState(ConnectState state) {
        connectState.set(state);
        kxListener.onStateUpdated(state);
    }

    private class LocalKxListener implements KxListener {
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
                // A KException can be caused by an invalid type on an update
                // An EOFException is when we lose connection to remote socket
                // not guaranteed for isConnection to reflect reality correctly - seen socket lie
                if (!connection.isConnected()) {
                    setConnectState(ConnectState.Connecting);
                    connectionAttempt = 0;
                    tryConnect(++connectionAttempt);
                }
            }
        }

        @Override
        public void onMessage(Object message) {
            kxListener.onMessage(message);
        }
    }
}
