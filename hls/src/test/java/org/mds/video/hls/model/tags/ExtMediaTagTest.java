package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtMediaTagTest {

    private static String line_pattern = "%s:%s=VIDEO,%s=\"low\",%s=\"Main\",%s=%s,%s=%s,%s=\"en\",%s=\"main/english-audio.m3u8\"";
    private static String line_pattern1 = "%s:%s=VIDEO,%s=\"low\",%s=\"Main\",%s=%s,%s=\"en\",%s=\"main/english-audio.m3u8\"";

    @Test
    public void test() {
        ExtMediaTag tag = ExtMediaTag.forLine(null);
        Assert.assertTrue(tag == null);

        String line = String.format(line_pattern,
                ExtMediaTag.EXTMEDIA_TAG, ExtMediaTag.ATTR_TYPE,
                ExtMediaTag.ATTR_GROUPID,
                ExtMediaTag.ATTR_NAME, ExtMediaTag.ATTR_DEFAULT,
                ExtMediaTag.DEFAULT_OPTION_YES, ExtMediaTag.ATTR_AUTOSELECT,
                ExtMediaTag.AUTOSELECT_OPTION_YES,
                ExtMediaTag.ATTR_LANGUAGE,
                ExtMediaTag.ATTR_URI);
        tag = ExtMediaTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getUri(), "main/english-audio.m3u8");
        Assert.assertEquals(tag.getGroupId(), "low");
        Assert.assertEquals(tag.getType(), "VIDEO");
        Assert.assertEquals(tag.getName(), "Main");
        Assert.assertEquals(tag.getLanguage(), "en");
        Assert.assertTrue(tag.isAsDefault());
        Assert.assertTrue(tag.isAutoSelect());
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());

        line = String.format(line_pattern1,
                ExtMediaTag.EXTMEDIA_TAG, ExtMediaTag.ATTR_TYPE,
                ExtMediaTag.ATTR_GROUPID,
                ExtMediaTag.ATTR_NAME, ExtMediaTag.ATTR_DEFAULT,
                ExtMediaTag.DEFAULT_OPTION_YES,
                ExtMediaTag.ATTR_LANGUAGE,
                ExtMediaTag.ATTR_URI);
        tag = ExtMediaTag.forLine(line);
        Assert.assertEquals(tag.getUri(), "main/english-audio.m3u8");
        Assert.assertEquals(tag.getGroupId(), "low");
        Assert.assertEquals(tag.getType(), "VIDEO");
        Assert.assertEquals(tag.getName(), "Main");
        Assert.assertEquals(tag.getLanguage(), "en");
        Assert.assertTrue(tag.isAsDefault());

        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
