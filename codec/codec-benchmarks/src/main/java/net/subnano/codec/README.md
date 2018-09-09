## Codec Benchmarks

**Average invocation time in nanoseconds.**
```
Benchmark                                                     Mode  Cnt     Score      Error   Units
JsonCodecBench.nullJsonParser                                 avgt    9   570.372 ±   14.423   ns/op
JsonCodecBench.nanoCodec                                      avgt    9  1251.710 ±   49.634   ns/op
JsonCodecBench.jacksonCodec                                   avgt    9  1624.739 ±   62.101   ns/op
```

**Object allocation rate (MB/sec).**
```
Benchmark                                                     Mode  Cnt     Score      Error   Units
JsonCodecBench.nullJsonParser:·gc.alloc.rate                  avgt    9    ≈ 10⁻⁴             MB/sec
JsonCodecBench.nanoCodec:·gc.alloc.rate                       avgt    9    ≈ 10⁻⁴             MB/sec
JsonCodecBench.jacksonCodec:·gc.alloc.rate                    avgt    9  1289.874 ±   48.652  MB/sec
```

**Throughput (Operations/sec).**
```
Benchmark                       Mode  Cnt    Score    Error  Units
JsonCodecBench.nullJsonParser  thrpt    9  1633428 ± 352828  ops/s
JsonCodecBench.nanoCodec       thrpt    9   813171 ±  17221  ops/s
JsonCodecBench.jacksonCodec    thrpt    9   635946 ±   8402  ops/s
```
