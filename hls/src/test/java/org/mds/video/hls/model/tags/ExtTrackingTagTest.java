package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Randall.mo on 14-4-25.
 */
public class ExtTrackingTagTest {
    @Test
    public void testAll() throws Exception {
        ExtTrackingTag tag = ExtTrackingTag.forLine(null);
        Assert.assertTrue(tag == null);

        String line = ExtTrackingTag.EXTTRACKING_TAG + ":"
                +ExtTrackingTag.ATTR_TRACKING_ID+"=12345678,"
                +ExtTrackingTag.ATTR_DURATION+"=30,"
                +ExtTrackingTag.ATTR_PSN+"=true";

        tag=ExtTrackingTag.forLine(line);
        Assert.assertEquals(tag.getTrackingId(),"12345678");
        Assert.assertEquals(tag.getDuration(),"30");
        Assert.assertEquals(tag.getPsn(),"true");

        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}