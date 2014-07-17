package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtTargetDurationTagTest {

    @Test
    public void test() {
        ExtTargetDurationTag tag = ExtTargetDurationTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtTargetDurationTag.EXTTARGETDURATION_TAG + ":6";
        tag = ExtTargetDurationTag.forLine(line);
        Assert.assertEquals(tag.getTargetDuration(), 6);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
