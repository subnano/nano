package net.subnano.kx;

import kx.c;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimeZone;

public class DefaultKxConnection implements KxConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKxConnection.class);

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    private final String host;
    private final int port;
    private final KxListener listener;


    private kx.c c;

    public DefaultKxConnection(String host, int port) {
        this(host, port, new NullKxListener());
    }

    public DefaultKxConnection(String host, int port, KxListener listener) {
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    @Override
    public void connect()  {
        LOGGER.info("Connecting to {}:{} ..", host, port);
        try {
            this.c = new c(host, port);
            this.c.tz = UTC_TIME_ZONE;
            LOGGER.info("Connected to {}:{}", host, port);
            listener.onStateUpdated(ConnectState.Connected);
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    @Override
    public <T> KxTableWriter<T> newTableWriter(KxWriterSource<T> kxWriterSource) {
        return new DefaultTableWriter<>(
                this,
                kxWriterSource.schema(),
                kxWriterSource.encoder(),
                kxWriterSource.mode()
        );
    }

    @Override
    public void sync(String table, String command, Object data) {
        try {
            LOGGER.trace("sync: table:{} command:{} date:{}", table, command, data);
            Object response = c.k(command, table, data);
            // TODO need a more sensible return object
            LOGGER.trace("Kx response: {}", response);
            listener.onMessage(response);
        } catch (Exception e) {
            LOGGER.error("Error writing record to kx: {}", e);
            listener.onError(e);
        }
    }

    @Override
    public void async(String table, String command, Object data) {
        try {
            LOGGER.trace("async: table:{} command:{} data:{}", table, command, data);
            c.ks(command, table, data);
        } catch (IOException e) {
            // commonly see a SocketException when kx process dies
            // TODO buffer record for offline persistence
            LOGGER.error("Error writing record to kx: {}", e);
            listener.onError(e);
        }
    }

    @Override
    public boolean isConnected() {
        return c != null && c.s != null && c.s.isConnected();
    }

    @Override
    public void close() {
        if (c != null) {
            try {
                c.close();
                listener.onStateUpdated(ConnectState.Closed);
            } catch (IOException e) {
                LOGGER.warn("Error closing socket connection: {}", e.getMessage());
                listener.onError(e);
            } finally {
                c = null;
            }
        }
    }

    @Override
    public Object subscribe(String table) {
        // TODO WIP since it's all been about writing data not too much thought gone into reading/subscription
        return null;
    }

    String host() {
        return host;
    }

    int port() {
        return port;
    }

}
