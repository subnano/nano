## Maths Functions
The benchmarks with the prefix `math` are the `java.lang.Math` implementations.

```
Benchmark             Mode  Cnt     Score    Error  Units
MathsBench.log10      avgt    9    34.636 ±  2.849  ns/op
MathsBench.pow10      avgt    9    22.241 ±  0.749  ns/op
MathsBench.pow        avgt    9    53.908 ±  1.311  ns/op
MathsBench.mathLog10  avgt    9   274.068 ±  5.422  ns/op
MathsBench.mathPow10  avgt    9  1356.301 ± 31.671  ns/op
MathsBench.mathPow    avgt    9  1589.436 ± 38.899  ns/op
```
**Observations**
* The Nano implementations of `log10`, `pow` and `pow10` are tuned for performance and are much quicker. 

## DateTime Encoding

```
Benchmark                                                         Mode  Cnt     Score     Error   Units
DateTimeBench.nanoDecoder                                         avgt    9    18.925 ±   0.878   ns/op
DateTimeBench.nanoEncoder                                         avgt    9    58.365 ±   3.464   ns/op
DateTimeBench.dateFormatDecoder                                   avgt    9   834.661 ±  23.500   ns/op
DateTimeBench.dateFormatEncoder                                   avgt    9   357.461 ±  26.768   ns/op

DateTimeBench.nanoDecoder:·gc.alloc.rate                          avgt    9     0.532 ±   0.017  MB/sec
DateTimeBench.nanoEncoder:·gc.alloc.rate                          avgt    9     0.523 ±   0.008  MB/sec
DateTimeBench.dateFormatDecoder:·gc.alloc.rate                    avgt    9   561.084 ±  15.574  MB/sec
DateTimeBench.dateFormatEncoder:·gc.alloc.rate                    avgt    9  1339.937 ± 101.528  MB/sec
```
**Observations**
* nano date/time encoding is blazing fast
* nano encoding still allocating something .. possibly the monitoring code itself (??)

## FIX Message Encoding

#### Still using SimpleDataFormat ??
```
....[Thread state: RUNNABLE]........................................................................
 50.0%  60.2% java.net.SocketInputStream.socketRead0
 10.6%  12.8% java.text.SimpleDateFormat.format
  9.1%  11.0% net.nanofix.message.NanoFixMessageBench.encodeHeartbeat
  4.7%   5.6% java.text.SimpleDateFormat.subFormat
  3.9%   4.7% java.text.DateFormatSymbols.<init>
  2.7%   3.3% java.text.DecimalFormat.format
  1.0%   1.2% java.nio.charset.CharsetEncoder.replaceWith
  0.4%   0.5% sun.nio.cs.US_ASCII.newEncoder
  0.2%   0.3% java.util.concurrent.ConcurrentHashMap.get
  0.2%   0.2% java.util.Arrays.copyOf
  0.2%   0.3% <other>

....[Thread state: WAITING].........................................................................
 16.9% 100.0% sun.misc.Unsafe.park

# Run complete. Total time: 00:00:38

Benchmark                                                             Mode  Cnt     Score     Error   Units
NanoFixMessageBench.encodeHeartbeat                                   avgt    9   747.310 ±  58.999   ns/op
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate                    avgt    9   887.177 ±  71.839  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate.norm               avgt    9  1040.623 ±   0.034    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space           avgt    9   890.093 ± 139.156  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space.norm      avgt    9  1044.150 ± 137.695    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space       avgt    9     0.076 ±   0.074  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space.norm  avgt    9     0.091 ±   0.091    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.count                         avgt    9    68.000            counts
NanoFixMessageBench.encodeHeartbeat:·gc.time                          avgt    9    66.000                ms
```

### Started using DateTimeEncoder
```
....[Thread state: RUNNABLE]........................................................................
 50.0%  60.1% java.net.SocketInputStream.socketRead0
 26.9%  32.3% net.nanofix.time.UtcDateTimeEncoder.encodeTime
  4.8%   5.8% net.nanofix.message.NanoFixMessageBench.encodeHeartbeat
  1.4%   1.7% net.nanofix.message.MessageHeader.populateBuffer
  0.0%   0.0% io.nano.core.buffer.ByteBufferUtil.asByteArray
  0.0%   0.0% net.nanofix.util.ByteArrayUtil.as1ByteArray

....[Thread state: WAITING].........................................................................
 16.9% 100.0% sun.misc.Unsafe.park

# Run complete. Total time: 00:00:38

Benchmark                                                             Mode  Cnt    Score    Error   Units
NanoFixMessageBench.encodeHeartbeat                                   avgt    9  928.005 ± 16.472   ns/op
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate                    avgt    9  113.624 ± 36.933  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate.norm               avgt    9  166.012 ± 54.248    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space           avgt    9  120.924 ± 15.547  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space.norm      avgt    9  176.572 ± 21.772    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space       avgt    9    0.053 ±  0.058  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space.norm  avgt    9    0.077 ±  0.085    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.count                         avgt    9   35.000           counts
NanoFixMessageBench.encodeHeartbeat:·gc.time                          avgt    9   31.000               ms
```

### Started using DateTimeEncoder
```

....[Thread state: RUNNABLE]........................................................................
 50.0%  59.9% java.net.SocketInputStream.socketRead0
 30.3%  36.3% net.nanofix.time.UtcDateTimeEncoder.encodeTime
  3.2%   3.8% net.nanofix.message.NanoFixMessageBench.encodeLogon
  0.0%   0.0% net.nanofix.message.generated.NanoFixMessageBench_encodeLogon_jmhTest.encodeLogon_avgt_jmhStub

# Run complete. Total time: 00:00:38

Benchmark                                            Mode  Cnt    Score    Error   Units
NanoFixMessageBench.encodeLogon                      avgt    9  875.527 ± 57.133   ns/op
NanoFixMessageBench.encodeLogon:·gc.alloc.rate       avgt    9    0.533 ±  0.008  MB/sec
NanoFixMessageBench.encodeLogon:·gc.alloc.rate.norm  avgt    9    0.734 ±  0.049    B/op
NanoFixMessageBench.encodeLogon:·gc.count            avgt    9      ≈ 0           counts
```
