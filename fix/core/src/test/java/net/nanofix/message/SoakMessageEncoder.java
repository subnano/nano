package net.nanofix.message;

import io.nano.core.lang.ByteString;

import java.nio.ByteBuffer;

// run with -javaagent:/home/mark/dev/aprof.jar
public class SoakMessageEncoder {

    private static final ByteString SENDER_COMP_ID = ByteString.of("CLIENT");
    private static final ByteString TARGET_COMP_ID = ByteString.of("BROKER");
    private static final long SENDING_TIME = System.currentTimeMillis();
    private static final ByteString USER = ByteString.of("user1");
    private static final ByteBuffer buffer = ByteBuffer.allocate(256);
    private static final ByteBuffer encodeBuffer = ByteBuffer.allocate(1024);


    public static void main(String[] args) {
        FIXMessage msg = new NanoFIXMessage(buffer);
        while (true) {
            encodeLogon(msg);
        }
    }

    private static void encodeLogon(FIXMessage msg) {
        buffer.clear();
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
        msg.encode(encodeBuffer, 0);
    }

}
