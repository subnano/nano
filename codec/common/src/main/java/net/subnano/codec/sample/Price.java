package net.subnano.codec.sample;

import io.nano.core.lang.ByteString;

/**
 * @author Mark Wardell
 */
public interface Price {

    ByteString getInstrument();

    long getTimeCreated();

    boolean isSuccess();

    Level[] getBids();

    Level[] getAsks();

}
