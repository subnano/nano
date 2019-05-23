package io.nano.core;

import io.nano.core.util.Strings;
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
public class StringBench {

    private static final String ARG_NAME = "Fred";

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(StringBench.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public void Strings_formatNumber(Blackhole hole) {
        hole.consume(Strings.formatNumber(123456.678905, 3));
    }

    @Benchmark
    public void Strings_combine(Blackhole hole) {
        hole.consume(Strings.combine("Some prefix for ", ARG_NAME, " with a suffix"));
    }

    @Benchmark
    public void String_formatNumber(Blackhole hole) {
        hole.consume(String.format("%.3f", 123456.678905));
    }

    @Benchmark
    public void String_formatCombine(Blackhole hole) {
        hole.consume(String.format("Some prefix for %s with a suffix", ARG_NAME));
    }

}
