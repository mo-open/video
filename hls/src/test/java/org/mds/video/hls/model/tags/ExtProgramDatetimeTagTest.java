package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtProgramDatetimeTagTest {

    @Test
    public void test() {
        ExtProgramDatetimeTag tag = ExtProgramDatetimeTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtProgramDatetimeTag.EXTPROGRAMDATETIME_TAG + ":2012-01";
        tag = ExtProgramDatetimeTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getProgramDateTime(), "2012-01");
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
