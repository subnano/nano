## Codec Benchmarks

Benchmarks comparing two JSON decoders, Jackson and Nano's own use of the `JsonByteParser` which used index overlay 
parsing technique for zero object allocation. A little extra effort is required to maintain the zero object allocation
pattern throughout but it doesn't take long and is trivial to test and refactor.

**Notes**
- Benchmarks are based on a ~280 byte JSON string which is pretty small and I would like to see some benchmarks on 
larger string.
- `strlenCodec` is purely for comparison to highlight the time taken to iterate through 
each byte of the same buffer.
- `nullJsonParser` is the least amount of work the `JsonByteParser` can perform and is included for comparison only.
 - Not only is the nano codec quicker, it allocates no objects at all so GC impact is minimized.
 - No Jackson optimizations have been attempted _(if any exist)_
 
**Average invocation time in nanoseconds.**
```
Benchmark                      Mode  Cnt     Score    Error  Units
JsonCodecBench.strlenCodec     avgt    9    14.706 ±  0.063  ns/op
JsonCodecBench.nullJsonParser  avgt    9   532.803 ±  6.046  ns/op
JsonCodecBench.nanoCodec       avgt    9  1193.657 ± 11.883  ns/op
JsonCodecBench.jacksonCodec    avgt    9  1584.691 ± 28.345  ns/op
```

**Object allocation rate (MB/sec).**
```
Benchmark                                     Mode  Cnt     Score      Error   Units
JsonCodecBench.nullJsonParser:·gc.alloc.rate  avgt    9    ≈ 10⁻⁴             MB/sec
JsonCodecBench.nanoCodec:·gc.alloc.rate       avgt    9    ≈ 10⁻⁴             MB/sec
JsonCodecBench.jacksonCodec:·gc.alloc.rate    avgt    9  1289.874 ±   48.652  MB/sec
```

**Throughput (Operations/millisecond).**
```
Benchmark                       Mode  Cnt      Score     Error   Units
JsonCodecBench.strlenCodec     thrpt    9  67956.813 ± 633.318  ops/ms
JsonCodecBench.nullJsonParser  thrpt    9   1869.954 ±  16.750  ops/ms
JsonCodecBench.nanoCodec       thrpt    9    832.945 ±  11.015  ops/ms
JsonCodecBench.jacksonCodec    thrpt    9    615.565 ±   5.129  ops/ms
```
