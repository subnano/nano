package io.nano.core;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.util.Maths;
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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class BufferBench {

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(BufferBench.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        String TEXT = "random text for wrapping in buffer";
    }

    @Benchmark
    public void ByteBufferUtil_wrap(BenchmarkState state, Blackhole hole) {
        hole.consume(ByteBufferUtil.wrap(state.TEXT));
    }

    @Benchmark
    public void ByteBuffer_wrap(BenchmarkState state, Blackhole hole) {
        hole.consume(ByteBuffer.wrap(state.TEXT.getBytes(StandardCharsets.US_ASCII)));
    }

}
