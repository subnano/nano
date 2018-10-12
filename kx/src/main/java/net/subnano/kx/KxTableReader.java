package net.subnano.kx;

import kx.c;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class KxTableReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(KxTableReader.class);

    private KxConnection kxConnection;
    private final kx.c c;
    private final String table;

    public KxTableReader(KxConnection kxConnection, c c, String table) {
        this.kxConnection = kxConnection;
        this.c = c;
        this.table = table;
    }

    /**
     * Polls the connection for the next record
     * @return the next record added to the database
     */
    Object next() {
        Object k = null;
        try {
            k = c.k();
        } catch (kx.c.KException e) {
            LOGGER.warn("Error returned from Kx: {}", e.toString());
        } catch (IOException e) {
            LOGGER.warn("IO Error returned from Kx: {}", e.toString());
        }
        return k;
    }
}
