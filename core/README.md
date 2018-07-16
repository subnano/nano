# Nano Core

A collection of classes used in the Nano series of ultra low latency
messaging library aiming for zero impact GC!

### TODO
- benchmark collections
- much, much more.
- add Koloboke to MapsBench
- improve maps interface (more methods)
- add comments why maps so fast (
-- uses pow2 not expensive mod
-- intint uses a single array so value lookup almost free
-- avoids use of extra array for cell state
- change free key to -1 and benchmark differences
- benchmark memory allocation of maps
- benchmark regular java hashmap
- toy with using single array for IntObj map
