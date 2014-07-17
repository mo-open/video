package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtSignalReplaceTagTest {

    private static String line_pattern = "%s:%s=123456,%s=6";
    @Test
    public void test() {
        ExtSignalReplaceTag tag = ExtSignalReplaceTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line=String.format(line_pattern,
                ExtSignalReplaceTag.EXTSIGNALREPLACE_TAG,
                ExtSignalReplaceTag.ATTR_SIGNALID,
                ExtSignalReplaceTag.ATTR_DURATION);

        tag = ExtSignalReplaceTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getSignalId(), "123456");
        Assert.assertEquals(tag.getDuration(), "6");
        Assert.assertEquals(tag.isBlackout(), true);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
