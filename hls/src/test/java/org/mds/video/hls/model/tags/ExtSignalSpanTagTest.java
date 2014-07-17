package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtSignalSpanTagTest {
    private static String line_pattern = "%s:%s=123456,%s=6";
    private static String line_pattern1 = "%s:%s=%s:123456,%s=6";

    @Test
    public void test() {
        ExtSignalSpanTag tag = ExtSignalSpanTag.forLine(null);
        Assert.assertTrue(tag == null);

        String line = String.format(line_pattern, ExtSignalSpanTag.EXTSIGNALSPAN_TAG,
                ExtSignalExitTag.ATTR_SIGNALID, ExtSignalExitTag.ATTR_DURATION);
        tag = ExtSignalSpanTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getSignalId(), "123456");
        Assert.assertEquals(tag.getDuration(), "6");
        Assert.assertEquals(tag.isBlackout(), false);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());

        line = String.format(line_pattern1, ExtSignalSpanTag.EXTSIGNALSPAN_TAG,
                ExtSignalExitTag.ATTR_SIGNALID,
                ExtSignalExitTag.SIGNALID_OPTION_BLACKOUT,
                ExtSignalExitTag.ATTR_DURATION);
        tag = ExtSignalSpanTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getSignalId(), "BLACKOUT:123456");
        Assert.assertEquals(tag.getDuration(), "6");
        Assert.assertEquals(tag.isBlackout(), true);
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
