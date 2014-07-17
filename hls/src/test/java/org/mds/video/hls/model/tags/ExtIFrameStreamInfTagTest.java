package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtIFrameStreamInfTagTest {

    private static String line_pattern1 = "%s:%s=1,%s=1280000,%s=\"test.ts\"";
    private static String line_pattern2 = "%s:%s=1,%s=65000,%s=\"mp4a.40.5,video/3gpp\",%s=640x480,%s=\"test.ts\"";

    @Test
    public void test() throws Exception {
        String line1 = String.format(line_pattern1,
                ExtIFrameStreamInfTag.EXTIFRAMESTREAMINF_TAG,
                ExtIFrameStreamInfTag.ATTR_PROGRAMID,
                ExtIFrameStreamInfTag.ATTR_BANDWIDTH,
                ExtIFrameStreamInfTag.ATTR_URI);
        ExtIFrameStreamInfTag tag = ExtIFrameStreamInfTag.forLine(line1);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getProgramId(), 1);
        Assert.assertEquals(tag.getBandwidth(), 1280000);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line1 + "\r\n", stringBuilder.toString());
        String line2 = "http://example.com/mid.m3u8";
        tag = ExtIFrameStreamInfTag.forLine(line2);
        Assert.assertTrue(tag == null);

        String line3 = String.format(line_pattern2,
                ExtIFrameStreamInfTag.EXTIFRAMESTREAMINF_TAG,
                ExtIFrameStreamInfTag.ATTR_PROGRAMID,
                ExtIFrameStreamInfTag.ATTR_BANDWIDTH,
                ExtIFrameStreamInfTag.ATTR_CODECS,
                ExtIFrameStreamInfTag.ATTR_RESOLUTION,
                ExtIFrameStreamInfTag.ATTR_URI);
        tag = ExtIFrameStreamInfTag.forLine(line3);
        Assert.assertEquals(tag.getProgramId(), 1);
        Assert.assertEquals(tag.getBandwidth(), 65000);
        Assert.assertEquals(tag.getResolution(), "640x480");
        Assert.assertEquals(tag.getCodecs(), "mp4a.40.5,video/3gpp");
        Assert.assertEquals(tag.getUri(), "test.ts");
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(line3 + "\r\n", stringBuilder.toString());
    }
}
