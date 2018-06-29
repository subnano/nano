package io.nano.time;

public class NanoTime {

    private static final String LIB_NAME = "nanotime";

    private static boolean libraryLoaded = false;

    static {
        NativeLoader.loadNativeLibrary(LIB_NAME);
    }

    /**
     * Returns the current wall clock time to microsecond precision
     * @return current tim in microseconds
     */
    public static native long currentTimeMicros();

    /**
     * Returns the current wall clock time to nanosecond precision
     * @return current tim in nanoseconds
     */
    public static native long currentTimeNanos();

}
