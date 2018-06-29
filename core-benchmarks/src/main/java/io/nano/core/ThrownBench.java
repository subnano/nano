package io.nano.core;

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

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class ThrownBench {

    private static final Optional<String> OPTION_OK = Optional.of("Pass");

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(ThrownBench.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public void methodThatThrows(Blackhole hole) {
        try {
            methodThrows();
        } catch (Exception e) {
            hole.consume(e);
        }
    }

    @Benchmark
    public void methodThatReturns(Blackhole hole) {
        hole.consume(methodReturns());
    }

    private void methodThrows() {
        throw new IllegalArgumentException("here");
    }

    private Optional<String> methodReturns() {
        return OPTION_OK;
    }

}
