package net.subnano.kx;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Not sure how going to automate testing to remote Kx process.
 * Might make this a manual test.
 */
@Disabled
class KxConnectionManagerTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KxConnectionManagerTest.class);

    private KxSample sample = new KxSample("Arthur Dent", 42, -1);
    private KxListener kxListener = new LocalKxListener();

    private KxListener spyListener = Mockito.spy(kxListener);
    private KxConnectionManager connection;
    private KxTableWriter<KxSample> writer;
    private ScheduledExecutorService scheduledExecutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        connection = new KxConnectionManager("localhost", 5001, 5_000, spyListener);
        writer = connection.newTableWriter(new KxSampleWriterSource());
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Test
    void connect() {
        connection.connect();

        verify(spyListener, timeout(99_000)).onStateUpdated(ConnectState.Connected);
        verify(spyListener, timeout(99_000)).onStateUpdated(ConnectState.Closed);
        connection.close();
    }

    private class LocalKxListener implements KxListener {
        @Override
        public void onStateUpdated(ConnectState state) {
            LOGGER.info("++ State: {}", state);
            if (ConnectState.Connected == state) {
                scheduledExecutor.scheduleAtFixedRate(() -> {
                    sample.time = System.currentTimeMillis();
                    writer.write(sample);
                }, 1000, 1000, TimeUnit.MILLISECONDS);
            }
            else if (ConnectState.Connecting == state){
            }
        }

        @Override
        public void onError(Throwable cause) {
            LOGGER.error("++ Error: {}", cause);
            scheduledExecutor.shutdown();
        }

        @Override
        public void onMessage(Object message) {
            LOGGER.info("++ Message: {}", message);

        }
    }
}