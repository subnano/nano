# Core Benchmarks

A collection of micro benchmarks measuring all sorts of code that has been optimized for particular use cases.

## return Code v Exception
```
Benchmark                      Mode  Cnt    Score    Error  Units
ThrownBench.methodThatReturns  avgt    9    2.889 ±  1.932  ns/op
ThrownBench.methodThatThrows   avgt    9  841.972 ± 42.323  ns/op
```

## Buffer Functions

```
Benchmark                                                         Mode  Cnt     Score     Error   Units
BufferBench.ByteBufferUtil_wrap                                   avgt    9    34.016 ±   1.288   ns/op
BufferBench.ByteBufferUtil_wrap:·gc.alloc.rate                    avgt    9  1944.076 ±  73.440  MB/sec
BufferBench.ByteBufferUtil_wrap:·gc.alloc.rate.norm               avgt    9   104.000 ±   0.001    B/op

BufferBench.ByteBuffer_wrap                                       avgt    9    48.346 ±   5.230   ns/op
BufferBench.ByteBuffer_wrap:·gc.alloc.rate                        avgt    9  2322.084 ± 250.075  MB/sec
BufferBench.ByteBuffer_wrap:·gc.alloc.rate.norm                   avgt    9   176.000 ±   0.001    B/op
```

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
Benchmark                                                                    Mode  Cnt     Score     Error   Units
DateTimeBench.nanoDateTimeEncoder                                            avgt    9    57.674 ±   1.179   ns/op
DateTimeBench.nanoDateTimeEncoder:·gc.alloc.rate                             avgt    9     0.522 ±   0.012  MB/sec

DateTimeBench.nanoDateTimeDecoder                                            avgt    9    18.786 ±   0.266   ns/op
DateTimeBench.nanoDateTimeDecoder:·gc.alloc.rate                             avgt    9     0.537 ±   0.009  MB/sec

DateTimeBench.java_lang_DateFormat_Encoder                                   avgt    9   515.570 ±  13.661   ns/op
DateTimeBench.java_lang_DateFormat_Encoder:·gc.alloc.rate                    avgt    9   937.523 ±  25.020  MB/sec

DateTimeBench.java_lang_DateFormat_parse                                     avgt    9  1120.245 ±  41.667   ns/op
DateTimeBench.java_lang_DateFormat_parse:·gc.alloc.rate                      avgt    9   590.808 ±  21.726  MB/sec

DateTimeBench.java_util_Date_parse                                           avgt    9   887.355 ±  63.908   ns/op
DateTimeBench.java_util_Date_parse:·gc.alloc.rate                            avgt    9  1286.039 ±  91.858  MB/sec
```

**Observations**
* nano date/time codec is blazing fast and _very close to_ zero gc   
* nano encoding still allocating something .. possibly the monitoring code itself (??)
