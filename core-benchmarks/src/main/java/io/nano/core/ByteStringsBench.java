package io.nano.core;

import io.nano.core.buffer.AsciiBufferUtil;
import io.nano.core.collection.ByteStringArray;
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
public class ByteStringsBench {

    public static final int CAPACITY = 1024;

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder().include(ByteStringsBench.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        ByteBuffer buffer = getBuffer();

        ByteStringArray byteStringArray = newByteStringsArray();

        private ByteBuffer getBuffer() {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            AsciiBufferUtil.putCharSequence("EURUSD", buffer);
            buffer.flip();  // ready to be read
            buffer.mark();  // remember position
            return buffer;
        }

        private ByteStringArray newByteStringsArray() {
            ByteStringArray stringArrays = new ByteStringArray(CAPACITY);
            stringArrays.getOrCreate(buffer, 0, 6);
            return stringArrays;
        }

    }


    @Benchmark
    public void byteStringArray(BenchmarkState state, Blackhole hole) {
        ByteStringArray map = state.byteStringArray;
        hole.consume(map.getOrCreate(state.buffer, 0, 6));
    }

    @Benchmark
    public void byteStringMap(BenchmarkState state, Blackhole hole) {
    }

}
