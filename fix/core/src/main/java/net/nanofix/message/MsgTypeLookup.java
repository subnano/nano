package net.nanofix.message;

import java.util.HashMap;
import java.util.Map;

public class MsgTypeLookup {

    // TOD replace this with primitive map to avoid boxing
    private static final Map<Byte, MsgType> cache = new HashMap<>();

    static {
        addToCache(MsgTypes.Logon);
        addToCache(MsgTypes.Logout);
        addToCache(MsgTypes.Heartbeat);
        addToCache(MsgTypes.NewOrderSingle);
        addToCache(MsgTypes.ExecutionReport);
        // TODO enumerate all and add all single byte fields
        // TODO convert multiple bytes to a short or an int
    }

    private static void addToCache(MsgType msgType) {
        cache.put(msgType.bytes()[0], msgType);
    }

    public static MsgType lookup(byte msgTypeChar) {
        return cache.get(msgTypeChar);
    }
}