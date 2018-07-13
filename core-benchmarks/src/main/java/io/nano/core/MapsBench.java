package io.nano.core;

import io.nano.core.collection.NanoIntIntMap;
import io.nano.core.util.Maths;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class MapsBench {

    public static final int CAPACITY = 1024;

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(MapsBench.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        Int2ObjectOpenHashMap fastUtilObjMap = new Int2ObjectOpenHashMap();
        NanoIntIntMap nanoIntIntMap = newNanoIntIntMap(CAPACITY, 0.75f);
        Int2IntArrayMap fastUtilMap = newInt2IntArrayMap();

        private Int2IntArrayMap newInt2IntArrayMap() {
            Int2IntArrayMap map = new Int2IntArrayMap(CAPACITY);
            for (int i=0; i<=256; i++)
                map.put(i, i);
            return map;
        }

        private NanoIntIntMap newNanoIntIntMap(int size, float fillFactor) {
            NanoIntIntMap map = new NanoIntIntMap(size, fillFactor);
            for (int i=0; i<=256; i++)
                map.put(i, i);
            return map;
        }
    }


    @Benchmark
    public void IntIntMap_nano_Get(BenchmarkState state,Blackhole hole) {
        NanoIntIntMap map = state.nanoIntIntMap;
        for (int i = 0; i <= map.size(); i++)
            hole.consume(map.get(i));
    }

    @Benchmark
    public void IntIntMap_fastutil_Get(BenchmarkState state,Blackhole hole) {
        Int2IntArrayMap map = state.fastUtilMap;
        for (int i = 0; i <= map.size(); i++)
            hole.consume(map.get(i));
    }

}
