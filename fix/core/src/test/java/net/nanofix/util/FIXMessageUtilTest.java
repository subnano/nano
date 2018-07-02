package net.nanofix.util;

import net.nanofix.message.Tags;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 22/03/12
 * Time: 16:25
 */
public class FIXMessageUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(FIXMessageUtilTest.class);

    static final int LOOP_COUNT = 10000000;
    static final String adminMsgTypes = "0A12345";
    static final String[] adminMsgTypeArray = {"0", "A", "1", "2", "3", "4", "5"};

    @Test
    public void testIsAdminMessage() {
        assertThat(FIXMessageUtil.isAdminMessage("0")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("A")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("1")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("2")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("3")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("4")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("5")).isTrue();
        assertThat(FIXMessageUtil.isAdminMessage("6")).isFalse();
        assertThat(FIXMessageUtil.isAdminMessage("a")).isFalse();
        assertThat(FIXMessageUtil.isAdminMessage("B")).isFalse();
    }

    @Test
    public void testHeaderFields() {
        assertThat(FIXMessageUtil.isHeaderField(Tags.BeginString)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.BodyLength)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.MsgType)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.SenderCompID)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.TargetCompID)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.MsgSeqNum)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.PossDupFlag)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.PossResend)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.SendingTime)).isTrue();
    }

    @Test
    public void testFix5HeaderFields() {
        assertThat(FIXMessageUtil.isHeaderField(Tags.ApplVerID)).isTrue();
        assertThat(FIXMessageUtil.isHeaderField(Tags.CstmApplVerID)).isTrue();
    }

    @Test
    public void testTrailerFields() {
        assertThat(FIXMessageUtil.isTrailerField(Tags.SignatureLength)).isTrue();
        assertThat(FIXMessageUtil.isTrailerField(Tags.Signature)).isTrue();
        assertThat(FIXMessageUtil.isTrailerField(Tags.CheckSum)).isTrue();
    }

    private static boolean isAdminMessageAsString(String msgType) {
        return msgType.length() == 1 && "0A12345".contains(msgType);
    }

}
