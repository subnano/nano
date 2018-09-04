package io.nano.core;

import io.nano.core.util.Maths;
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

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class MathsBench {

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(MathsBench.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public void pow10(Blackhole hole) {
        for (int i = 0; i <= 9; i++)
            hole.consume(Maths.pow10(i));
    }

    @Benchmark
    public void pow(Blackhole hole) {
        for (int i = 0; i <= 12; i++)
            hole.consume(Maths.pow(i, 3));
    }

    @Benchmark
    public void numberDigits(Blackhole hole) {
        for (int i = 0; i <= 9; i++)
            hole.consume(Maths.numberDigits(Maths.pow10(i) + i));
    }

    @Benchmark
    public void mathPow10(Blackhole hole) {
        for (int i = 0; i <= 9; i++)
            hole.consume(Math.pow(i, 10));
    }

    @Benchmark
    public void mathPow(Blackhole hole) {
        for (int i = 0; i <= 12; i++)
            hole.consume(Math.pow(i, 3));
    }

    @Benchmark
    public void mathLog10(Blackhole hole) {
        for (int i = 0; i <= 9; i++)
            hole.consume(Math.log10((double) Maths.pow10(i) + i));
    }
}
