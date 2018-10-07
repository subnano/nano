package net.nanofix.message;

import io.nano.core.time.UtcDateTimeDecoder;
import io.nano.core.lang.ByteString;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class LogonMessageReaderTest {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private LogonMessageReader reader = new LogonMessageReader();

    @Test
    void readLogonMessage() {
        String msgText = FIXMessageStrings.LOGON_RESET;
        MessageTestHelper.prepareBuffer(buffer, msgText);
        decoder.decode(buffer, reader);
        LogonMessageReaderAssert.assertThat(reader)
                .hasEncryptMethod(false)
                .hasHeartBeatInterval(30)
                .hasResetSeqNumFlag(true)
                .header()
                .hasMsgType(MsgTypes.Logon)
                .hasMsgSeqNum(1)
                .hasSenderCompId(ByteString.of("BANZAI"))
                .hasTargetCompId(ByteString.of("EXEC"))
                .hasSendingTime(UtcDateTimeDecoder.decode("20120331-10:25:15.000"));
    }

}