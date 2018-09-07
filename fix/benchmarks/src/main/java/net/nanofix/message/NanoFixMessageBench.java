package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.time.TimeUtil;
import io.nano.core.util.ByteArrayUtil;
import io.nano.core.lang.ByteString;
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
import org.openjdk.jmh.profile.StackProfiler;
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
public class NanoFixMessageBench {

    static final String LOGON_RESET = "8=FIX.4.0|9=61|35=A|34=1|49=BANZAI|52=20120331-10:25:15.000|56=EXEC|98=0|108=30|141=Y|10=255|";

    private static final ByteString SENDER_COMP_ID = ByteString.of("CLIENT");
    private static final ByteString TARGET_COMP_ID = ByteString.of("BROKER");
    private static final long SENDING_TIME = System.currentTimeMillis();
    private static final ByteString USER = ByteString.of("user1");
    private static final ByteString CL_ORD_ID = ByteString.of("ID12345");
    private static final ByteString EURUSD = ByteString.of("EURUSD");
    private static final long TRANSACT_TIME = TimeUtil.toEpochDay(2000, 12, 25);

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.ignoreLock", "true");
        Options options = new OptionsBuilder()
                .include(NanoFixMessageBench.class.getSimpleName())
                .addProfiler(StackProfiler.class)
                .addProfiler(GCProfiler.class)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        ByteBuffer encodeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        FIXMessage msg = new NanoFIXMessage(buffer);
        NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
        LogonMessageReader reader = new LogonMessageReader();
        ByteBuffer decodeBuffer = NanoFixMessageBench.createBuffer(LOGON_RESET);
    }

    @Benchmark
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

    @Benchmark
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

    @Benchmark
    public void decodeLogonMessage(BenchmarkState state) {
        state.decodeBuffer.reset();
        state.decoder.decode(state.decodeBuffer, state.reader);
    }

    static ByteBuffer createBuffer(String msgString) {
        ByteBuffer buffer = ByteBuffer.allocate(msgString.length());
        ByteBufferUtil.putBytes(buffer, ByteArrayUtil.asByteArray(msgString.replaceAll("\\|", String.valueOf((char) FIXBytes.SOH))));
        buffer.flip();
        buffer.mark();
        return buffer;
    }
}
