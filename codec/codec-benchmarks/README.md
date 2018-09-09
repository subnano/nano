## Codec Benchmarks

**Average invocation time in nanoseconds.**
```
Benchmark                      Mode  Cnt     Score    Error  Units
JsonCodecBench.strlenCodec     avgt    9    14.794 ±  0.119  ns/op
JsonCodecBench.nullJsonParser  avgt    9   566.573 ±  6.412  ns/op
JsonCodecBench.nanoCodec       avgt    9  1232.686 ± 19.798  ns/op
JsonCodecBench.jacksonCodec    avgt    9  1592.831 ± 34.925  ns/op
```

**Object allocation rate (MB/sec).**
```
Benchmark                                     Mode  Cnt     Score      Error   Units
JsonCodecBench.nullJsonParser:·gc.alloc.rate  avgt    9    ≈ 10⁻⁴             MB/sec
JsonCodecBench.nanoCodec:·gc.alloc.rate       avgt    9    ≈ 10⁻⁴             MB/sec
JsonCodecBench.jacksonCodec:·gc.alloc.rate    avgt    9  1289.874 ±   48.652  MB/sec
```

**Throughput (Operations/sec).**
```
Benchmark                       Mode  Cnt    Score    Error  Units
JsonCodecBench.nullJsonParser  thrpt    9  1633428 ± 352828  ops/s
JsonCodecBench.nanoCodec       thrpt    9   813171 ±  17221  ops/s
JsonCodecBench.jacksonCodec    thrpt    9   635946 ±   8402  ops/s
```
