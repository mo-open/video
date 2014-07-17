package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtAllowCacheTagTest {


    @Test
    public void test() {
        ExtAllowCacheTag tag = ExtAllowCacheTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtAllowCacheTag.EXTALLOWCACHE_TAG + ":" + ExtAllowCacheTag.OPTION_YES;
        tag = ExtAllowCacheTag.forLine(line);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertTrue(tag != null);
        Assert.assertTrue(tag.isAllowCache());
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
