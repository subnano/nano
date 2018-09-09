package net.subnano.codec.json.sample;

/**
 * @author Mark Wardell
 */
public interface Price {

    String getInstrument();

    long getTimestamp();

    boolean isSuccess();

    Level[] getBids();

    Level[] getAsks();

}
