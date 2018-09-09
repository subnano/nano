package net.subnano.codec;

import io.nano.core.buffer.AsciiBufferUtil;
import net.subnano.codec.json.sample.JacksonPriceCodec;
import net.subnano.codec.json.sample.MutablePrice;
import net.subnano.codec.json.sample.NanoPriceCodec;
import net.subnano.codec.json.sample.Price;
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static net.subnano.codec.json.sample.SampleData.SAMPLE_PRICE;

/**
 * @author Mark Wardell
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class JsonCodecBench {

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder().include(JsonCodecBench.class.getSimpleName())
                //.addProfiler(GCProfiler.class)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        JacksonPriceCodec jacksonCodec = new JacksonPriceCodec();
        NanoPriceCodec nanoCodec = new NanoPriceCodec();
        NullBufferCodec nullCodec = new NullBufferCodec();
        StrlenBufferCodec strlenCodec = new StrlenBufferCodec();
        ByteBuffer buffer = getBuffer();
        MutablePrice mutablePrice = new MutablePrice();

        private ByteBuffer getBuffer() {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            AsciiBufferUtil.putCharSequence(SAMPLE_PRICE, buffer);
            buffer.flip();  // ready to be read
            buffer.mark();  // remember position
            return buffer;
        }
    }

    @Benchmark
    public void jacksonCodec(BenchmarkState state, Blackhole hole) throws IOException {
        state.buffer.reset();
        Price price = state.jacksonCodec.decode(state.buffer);
        hole.consume(price);
    }

    @Benchmark
    public void nullJsonParser(BenchmarkState state) {
        state.buffer.reset();
        state.nullCodec.decode(state.buffer);
    }

    @Benchmark
    public void strlenCodec(BenchmarkState state, Blackhole hole) {
        state.buffer.reset();
        hole.consume(state.strlenCodec.decode(state.buffer));
    }

    @Benchmark
    public void nanoCodec(BenchmarkState state) {
        state.buffer.reset();
        state.nanoCodec.decode(state.buffer, state.mutablePrice);
    }

    /**
     * SoakRunner used to invoke a method continuously so can be profiled externally
     */
    static class SoakRunner {

        public static void main(String[] args) {
            BenchmarkState state = new BenchmarkState();
            JsonCodecBench bench = new JsonCodecBench();
            while (true) {
                bench.nullJsonParser(state);
            }
        }
    }
}

