package net.nanofix.message;

import io.nano.core.time.UtcDateTimeDecoder;
import io.nano.core.time.UtcDateTimeEncoder;
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
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.LongConsumer;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class DateTimeBench {

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(DateTimeBench.class.getSimpleName())
                .addProfiler(StackProfiler.class)
                .addProfiler(GCProfiler.class)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        long[] lastTime = new long[]{0};
        LongConsumer consumer = x -> lastTime[0] = x;
        ByteBuffer buffer = ByteBuffer.allocate(256);
        long currentTimeMillis = System.currentTimeMillis();
        UtcDateTimeEncoder nanoEncoder = new UtcDateTimeEncoder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        //String source = "2018-08-06T10:49:02.971Z";

        Date date = new Date();
        String utcDateString;

        public BenchmarkState() {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            date.setTime(currentTimeMillis);
            utcDateString = dateFormat.format(date);
            date.setTime(0);
        }
    }

    @Benchmark
    public void nanoDateTimeEncoder(BenchmarkState state) {
        state.buffer.clear();
        state.nanoEncoder.encode(state.currentTimeMillis, state.buffer, 0);
    }

    @Benchmark
    public void nanoDateTimeDecoder(BenchmarkState state) {
        state.buffer.clear();
        state.consumer.accept(UtcDateTimeDecoder.decode(state.buffer, 0));
    }

    @Benchmark
    public void java_lang_DateFormat_Encoder(BenchmarkState state) {
        state.buffer.clear();
        state.date.setTime(state.currentTimeMillis);
        String utcDate = state.dateFormat.format(state.date);
        state.buffer.put(utcDate.getBytes(StandardCharsets.US_ASCII));
    }

    @Benchmark
    public void java_lang_DateFormat_parse(BenchmarkState state, Blackhole hole) {
        try {
            Date parsedDate = state.dateFormat.parse(state.utcDateString);
            hole.consume(parsedDate.getTime());
        } catch (ParseException e) {
            // do nothing
        }
    }

    @Benchmark
    public void java_util_Date_parse(BenchmarkState state, Blackhole hole) {
        Date parsedDate = Date.from(Instant.parse(state.utcDateString));
        hole.consume(parsedDate.getTime());
    }

    static class SoakRunner {

        public static void main(String[] args) {
            BenchmarkState state = new BenchmarkState();
            DateTimeBench bench = new DateTimeBench();
            while (true) {
                bench.nanoDateTimeDecoder(state);
            }
        }
    }
}
