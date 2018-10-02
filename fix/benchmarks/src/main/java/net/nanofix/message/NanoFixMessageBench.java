package net.nanofix.message;

import io.nano.core.lang.ByteString;
import io.nano.core.time.TimeUtil;
import io.nano.core.util.ByteArrayUtil;
import net.nanofix.util.FIXBytes;
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
import java.util.concurrent.TimeUnit;

import static net.nanofix.message.NanoFixBenchData.LOGON;
import static net.nanofix.message.NanoFixBenchData.MD_SNAPSHOT;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
@Fork(3)
public class NanoFixMessageBench {

    private static final ByteString SENDER_COMP_ID = ByteString.of("CLIENT");
    private static final ByteString TARGET_COMP_ID = ByteString.of("BROKER");
    private static final long SENDING_TIME = System.currentTimeMillis();
    private static final ByteString USER = ByteString.of("user1");
    private static final ByteString CL_ORD_ID = ByteString.of("ID12345");
    private static final ByteString EURUSD = ByteString.of("EURUSD");
    private static final long TRANSACT_TIME = TimeUtil.toEpochDay(2000, 12, 25);

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder().include(NanoFixMessageBench.class.getSimpleName())
                //.addProfiler(StackProfiler.class)
                .addProfiler(GCProfiler.class).build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        ByteBuffer encodeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        FIXMessage msg = new NanoFIXMessage(buffer);
        NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
        NanoFIXMessageDecoder2 decoder2 = new NanoFIXMessageDecoder2();
        LogonMessageReader logonReader = new LogonMessageReader();
        LogonMessageReader2 logonReader2 = new LogonMessageReader2();
        ByteBuffer decodeBuffer = NanoFixBenchData.createBuffer(LOGON);
        ByteBuffer marketDataBuffer = NanoFixBenchData.createBuffer(MD_SNAPSHOT);
        byte[] intBytes = ByteArrayUtil.asByteArray(743);
    }

    //@Benchmark
    public void encodeLogon(BenchmarkState state, Blackhole hole) {
        state.buffer.clear();
        FIXMessage msg = state.msg;
        msg.header().beginString(BeginStrings.FIX_4_2);
        msg.header().msgType(MsgTypes.Logon);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(42);
        msg.header().sendingTime(SENDING_TIME);
        msg.addIntField(Tags.EncryptMethod, 0);
        msg.addIntField(Tags.HeartBtInt, 30);
        msg.addBooleanField(Tags.ResetSeqNumFlag, true);
        msg.addStringField(Tags.Username, USER);
        hole.consume(msg.encode(state.encodeBuffer, 0));
    }

    //@Benchmark
    public void encodeNewOrderSingle(BenchmarkState state, Blackhole hole) {
        // 8=FIX.4.4|9=132|35=D|34=4|49=BANZAI|52=20120331-10:26:33.264|56=EXEC|11=1333189593005|21=1|38=100|40=1|54=1|
        // 55=GOOG.N|59=0|60=20120331-10:26:33.257|10=219|
        FIXMessage msg = state.msg;
        msg.header().beginString(BeginStrings.FIX_4_2);
        msg.header().msgType(MsgTypes.NewOrderSingle);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(42);
        msg.header().sendingTime(SENDING_TIME);
        // 11 21 38 40 54 55 59 60
        msg.addStringField(Tags.ClOrdID, CL_ORD_ID);
        msg.addByteField(Tags.HandlInst, FIXBytes.HANDL_INST_AUTO);
        msg.addLongField(Tags.OrderQty, 10_000);
        msg.addByteField(Tags.OrdType, FIXBytes.ORD_TYPE_MARKET);
        msg.addByteField(Tags.Side, FIXBytes.SIDE_SELL);
        msg.addStringField(Tags.Symbol, EURUSD);
        msg.addByteField(Tags.TimeInForce, FIXBytes.TIF_DAY);
        msg.addTimestamp(Tags.TransactTime, TRANSACT_TIME);
        hole.consume(msg.encode(state.encodeBuffer, 0));
    }

    //@Benchmark
    public void decodeLogonMessage(BenchmarkState state) {
        //state.decodeBuffer.reset();
        state.logonReader.clear();
        state.decoder.decode(state.decodeBuffer, state.logonReader);
    }

    @Benchmark
    public void decodeMarketDataSnapshot(BenchmarkState state) {
        //state.marketDataBuffer.reset();
        state.logonReader.clear();
        state.decoder.decode(state.marketDataBuffer, state.logonReader);
    }

    @Benchmark
    public void decodeMarketDataSnapshot2(BenchmarkState state) {
        //state.marketDataBuffer.reset();
        state.logonReader2.clear();
        state.decoder2.decode(state.marketDataBuffer, state.logonReader2);
    }

    static class SoakRunner {

        public static void main(String[] args) {
            BenchmarkState state = new BenchmarkState();
            NanoFixMessageBench bench = new NanoFixMessageBench();
            while (true) {
                bench.decodeMarketDataSnapshot2(state);
            }
        }
    }

}
