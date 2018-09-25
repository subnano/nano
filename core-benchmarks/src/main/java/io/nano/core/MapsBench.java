package io.nano.core;

import io.nano.core.collection.IntIntMap;
import io.nano.core.collection.IntObjectMap;
import io.nano.core.collection.NanoIntIntMap;
import io.nano.core.collection.NanoIntObjectMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class MapsBench {

    public static final int SIZE_TO_FILL = 512;
    public static final int CAPACITY = 1024;
    public static final float FILL_FACTOR = 0.75f;

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options =
                new OptionsBuilder().include(MapsBench.class.getSimpleName()).addProfiler(GCProfiler.class).build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        IntIntMap nanoIntMap = newNanoIntIntMap(SIZE_TO_FILL, CAPACITY, FILL_FACTOR);
        Int2IntArrayMap fastUtilIntMap = newInt2IntArrayMap(SIZE_TO_FILL, CAPACITY);
        Int2ObjectMap fastUtilObjectMap = newInt2ObjectOpenHashMap(SIZE_TO_FILL, CAPACITY, FILL_FACTOR);
        IntObjectMap<String> nanoObjectMap = newNanoIntObjectMap(SIZE_TO_FILL, CAPACITY, FILL_FACTOR);

        int index = 0;

        private Int2IntArrayMap newInt2IntArrayMap(int sizeToFill, int capacity) {
            Int2IntArrayMap map = new Int2IntArrayMap(capacity);
            for (int i = 0; i <= sizeToFill; i++) {
                map.put(i, i);
            }
            return map;
        }
        private Int2ObjectMap newInt2ObjectOpenHashMap(int sizeToFill, int capacity, float fillFactor) {
            Int2ObjectMap map = new Int2ObjectOpenHashMap(capacity, fillFactor);
            for (int i = 0; i <= sizeToFill; i++) {
                map.put(i, String.valueOf(i));
            }
            return map;
        }

        private IntIntMap newNanoIntIntMap(int sizeToFill, int capacity, float fillFactor) {
            NanoIntIntMap map = new NanoIntIntMap(capacity, fillFactor);
            for (int i = 0; i <= sizeToFill; i++) {
                map.put(i, i);
            }
            return map;
        }

        private IntObjectMap<String> newNanoIntObjectMap(int sizeToFill, int capacity, float fillFactor) {
            IntObjectMap<String> map = new NanoIntObjectMap<>(capacity, fillFactor);
            for (int i = 0; i <= sizeToFill; i++) {
                map.put(i, String.valueOf(i));
            }
            return map;
        }
    }

    @Benchmark
    public void IntIntMap_nano_Get(BenchmarkState state, Blackhole hole) {
        IntIntMap map = state.nanoIntMap;
        hole.consume(map.get(state.index));
        state.index = state.index < SIZE_TO_FILL - 1 ? state.index++ : 0;
    }

    @Benchmark
    public void IntObjectMap_nano_Get(BenchmarkState state, Blackhole hole) {
        IntObjectMap<String> map = state.nanoObjectMap;
        hole.consume(map.get(state.index));
        state.index = state.index < SIZE_TO_FILL - 1 ? state.index++ : 0;
    }

    @Benchmark
    public void IntObjectMap_fastutil_Get(BenchmarkState state, Blackhole hole) {
        Int2ObjectMap map = state.fastUtilObjectMap;
        hole.consume(map.get(state.index));
        state.index = state.index < SIZE_TO_FILL - 1 ? state.index++ : 0;
    }

    @Benchmark
    public void IntIntMap_fastutil_Get(BenchmarkState state, Blackhole hole) {
        Int2IntArrayMap map = state.fastUtilIntMap;
        hole.consume(map.get(state.index));
        state.index = state.index < SIZE_TO_FILL - 1 ? state.index++ : 0;
    }

}
