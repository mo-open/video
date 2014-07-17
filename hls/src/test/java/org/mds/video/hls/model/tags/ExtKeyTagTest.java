package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtKeyTagTest {

    @Test
    public void test() {
        ExtKeyTag tag = ExtKeyTag.forLine(null);
        Assert.assertTrue(tag == null);

        String line = String.format("%s:%s=AES-128,%s=\"test.key\",%s=1",
                ExtKeyTag.EXTKEY_TAG, ExtKeyTag.ATTR_METHOD,
                ExtKeyTag.ATTR_URI, ExtKeyTag.ATTR_IV);

        tag = ExtKeyTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getMethod(), "AES-128");
        Assert.assertEquals(tag.getUri(), "test.key");
        Assert.assertEquals(tag.getIv(), "1");
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());

        line = String.format("%s:%s=NONE", ExtKeyTag.EXTKEY_TAG, ExtKeyTag.ATTR_METHOD);
        tag = ExtKeyTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getMethod(), "NONE");
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());

    }
}
