package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtVersionTagTest {

    @Test
    public void test() {
        ExtVersionTag tag = ExtVersionTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtVersionTag.EXTVERSION_TAG + ":1";
        tag = ExtVersionTag.forLine(line);
        Assert.assertEquals(tag.getVersion(), 1);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
