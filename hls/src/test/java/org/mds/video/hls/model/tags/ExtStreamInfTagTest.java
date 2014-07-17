package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtStreamInfTagTest {

    private static String line_pattern="%s:%s=1,%s=1280000";
    private static String line_pattern1="%s:%s=1,%s=65000,%s=\"mp4a.40.5,video/3gpp\",%s=640x480";;

    @Test
    public void test() throws Exception {
        String line1=String.format(line_pattern,ExtStreamInfTag.EXTSTREAMINF_TAG,
                ExtStreamInfTag.ATTR_PROGRAMID,
                ExtStreamInfTag.ATTR_BANDWIDTH);
        ExtStreamInfTag tag = ExtStreamInfTag.forLine(line1);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getProgramId(), 1);
        Assert.assertEquals(tag.getBandwidth(), 1280000);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line1 + "\r\n", stringBuilder.toString());
        String line2 = "http://example.com/mid.m3u8";
        tag = ExtStreamInfTag.forLine(line2);
        Assert.assertTrue(tag == null);

        String line3=String.format(line_pattern1,ExtStreamInfTag.EXTSTREAMINF_TAG,
                ExtStreamInfTag.ATTR_PROGRAMID,
                ExtStreamInfTag.ATTR_BANDWIDTH,
                ExtStreamInfTag.ATTR_CODECS,
                ExtStreamInfTag.ATTR_RESOLUTION);
        tag = ExtStreamInfTag.forLine(line3);
        Assert.assertEquals(tag.getProgramId(), 1);
        Assert.assertEquals(tag.getBandwidth(), 65000);
        Assert.assertEquals(tag.getResolution(), "640x480");
        Assert.assertEquals(tag.getCodecs(), "mp4a.40.5,video/3gpp");
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(line3 + "\r\n", stringBuilder.toString());
        tag.setUri("test.ts");
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(line3 + "\r\n" + "test.ts\r\n", stringBuilder.toString());
    }
}
