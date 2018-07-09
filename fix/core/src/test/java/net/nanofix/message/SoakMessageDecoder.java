package net.nanofix.message;

import java.nio.ByteBuffer;

// run with -javaagent:/home/mark/dev/aprof.jar
public class SoakMessageDecoder {

    private static final NanoFIXMessageDecoder messageDecoder = new NanoFIXMessageDecoder();
    private static final LogonMessageReader logonReader = new LogonMessageReader();


    public static void main(String[] args) {
        ByteBuffer decodeBuffer = MessageTestHelper.createBuffer(FIXMessageStrings.LOGON_RESET);
        while (true) {
            decodeLogonMessage(decodeBuffer);
        }
    }

    public static void decodeLogonMessage(ByteBuffer decodeBuffer) {
        decodeBuffer.position(0);
        messageDecoder.decode(decodeBuffer, logonReader);
    }

}
