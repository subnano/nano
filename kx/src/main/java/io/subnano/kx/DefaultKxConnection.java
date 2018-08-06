package io.subnano.kx;

import kx.c;
import kx.c.KException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimeZone;

public class DefaultKxConnection implements KxConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKxConnection.class);

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final KxListener listener;

    private final String userPassword;

    private kx.c c;

    public DefaultKxConnection(String host, int port) {
        this(host, port, null, null, new NullKxListener());
    }

    public DefaultKxConnection(String host, int port, String user, String password, KxListener listener) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.listener = listener;
        this.userPassword = userPassword();
    }

    @Override
    public void connect()  {
        LOGGER.info("Connecting to {}:{} ..", host, port);
        try {
            this.c = new c(host, port);
            this.c.tz = UTC_TIME_ZONE;
            LOGGER.info("Connected to {}:{}", host, port);
            listener.onConnect();
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    @Override
    public <T> KxTableWriter<T> newTableWriter(KxSchema kxSchema, KxEncoder<T> encoder, Mode mode) {
        return new DefaultTableWriter<>(
                this,
                kxSchema.tableName(),
                kxSchema.columnNames(),
                kxSchema.data(),
                encoder,
                mode
        );
    }

    @Override
    public void sync(String table, String command, kx.c.Flip flip) {
        try {
            Object result = c.k(command, table, flip);
            //LOGGER.debug("Sync method returned {}", result);
            // TODO need a more sensible return object
            listener.onMessage(result);
        } catch (Exception e) {
            LOGGER.error("Error writing record to kx", e);
            listener.onError(e);
        }
    }

    @Override
    public void async(String table, String command, kx.c.Flip flip) {
        try {
            c.ks(command, table, flip);
        } catch (IOException e) {
            LOGGER.error("Error writing record to kx", e);
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
                listener.onClose();
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

    private String userPassword() {
        if (user == null || user.length() == 0)
            return null;
        if (password == null || password.length() == 0)
            return user;
        return user + ":" + password;
    }

}
