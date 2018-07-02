package io.nano.core.time;

import java.time.Year;
import java.util.concurrent.TimeUnit;

public final class TimeUtil {

    public static long MILLIS_PER_MINUTE = TimeUnit.MINUTES.toMillis(1);
    public static long MILLIS_PER_HOUR = TimeUnit.HOURS.toMillis(1);
    public static long MILLIS_PER_DAY = TimeUnit.DAYS.toMillis(1);
    public static final int DAYS_UNTIL_START_OF_UNIX_EPOCH = 719528;
    public static final int MONTHS_IN_YEAR = 12;
    public static final int DAYS_IN_YEAR = 365;

    private TimeUtil() {
        // can't touch this
    }

    public static long epochDays(long epochTime, TimeUnit timeUnit) {
        return timeUnit.toDays(epochTime);
    }

    public static long millisInDay(long epochMillis) {
        return Math.floorMod(epochMillis, MILLIS_PER_DAY);
    }

    // Based on code from https://github.com/ThreeTen
    public static long estimateDayOfYear(final long zeroDay, final long yearEst) {
        return zeroDay - (365 * yearEst + yearEst / 4 - yearEst / 100 + yearEst / 400);
    }

    public static int toEpochDay(final int year, final int month, final int day) {
        return yearsToDays(year) + monthsToDays(month, year) + (day - 1) - DAYS_UNTIL_START_OF_UNIX_EPOCH;
    }

    private static int monthsToDays(final int month, final int year) {
        int days = (367 * month - 362) / MONTHS_IN_YEAR;
        if (month > 2) {
            days--;
            if (!Year.isLeap(year)) {
                days--;
            }
        }
        return days;
    }

    private static int yearsToDays(final int years) {
        return DAYS_IN_YEAR * years + (years + 3) / 4 - (years + 99) / 100 + (years + 399) / 400;
    }

}
