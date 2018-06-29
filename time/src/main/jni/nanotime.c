#include <time.h>
#include <sys/time.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <jni.h>
#include "nanotime.h"


#define NANOS_PER_SECOND    1000000000
#define MICROS_PER_SECOND   1000000
#define NANOS_PER_MICRO     1000

JNIEXPORT jlong JNICALL
Java_io_nano_time_NanoTime_currentTimeMicros(JNIEnv *env, jobject thisObj) {
#if defined(_POSIX_TIMERS)
  {
    struct timespec ts;
    if ( 0 == clock_gettime(CLOCK_REALTIME, &ts) )
    {
      return ((((jlong) ts.tv_sec) * MICROS_PER_SECOND) + (((jlong) ts.tv_nsec)) / NANOS_PER_MICRO);
    }
  }
#endif

    // otherwise use gettimeofday
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return (((jlong) tv.tv_sec) * MICROS_PER_SECOND) + ((jlong) tv.tv_usec);
}

JNIEXPORT jlong JNICALL
Java_io_nano_time_NanoTime_currentTimeNanos(JNIEnv *env, jobject thisObj) {
#if defined(_POSIX_TIMERS)
  {
    struct timespec ts;
    if ( 0 == clock_gettime(CLOCK_REALTIME, &ts) )
    {
      return (((jlong) ts.tv_sec) * NANOS_PER_SECOND) + (((jlong) ts.tv_nsec));
    }
  }
#endif

    // otherwise use gettimeofday
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return (((jlong) tv.tv_sec) * NANOS_PER_SECOND) + (((jlong) tv.tv_usec) * NANOS_PER_MICRO);
}
