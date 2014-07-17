package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtByteRangeTagTest {
    @Test
    public void test() {
        ExtByteRangeTag tag = ExtByteRangeTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtByteRangeTag.EXTBYTERANGE_TAG + ":100@200";
        tag = ExtByteRangeTag.forLine(line);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getLength(), 100);
        Assert.assertEquals(tag.getOffset(), 200);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
