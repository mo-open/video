package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mds.video.hls.model.tags.ExtFaxCmTag.*;

/**
 * @author Randall.mo
 */
public class ExtFaxCmTagTest {
    @Test
    public void test() {
        ExtFaxCmTag tag = ExtFaxCmTag.forLine(null);
        Assert.assertTrue(tag == null);

        String line= EXTFAXCM_TAG +":"+ATTR_URI+"=\"a.hldf\"";
        tag= ExtFaxCmTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getUri(),"a.hldf");
        Assert.assertNull(tag.getContent());
        Assert.assertFalse(tag.isForInternal());
        StringBuilder stringBuilder=new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(stringBuilder.toString(), line+"\r\n");

        tag.setForInternal(true);
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(stringBuilder.toString(),line+"\r\n");

        String line2= EXTFAXCM_TAG +":123456";
        tag.setContent("123456");
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(stringBuilder.toString(),line2+"\r\n");

        tag.setForInternal(false);
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(stringBuilder.toString(),line+"\r\n");

        tag.setUri(null);
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(stringBuilder.toString(),line2+"\r\n");

        tag=ExtFaxCmTag.forLine(line2);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getContent(),"123456");
        Assert.assertNull(tag.getUri());
        Assert.assertTrue(tag.isForInternal());
        stringBuilder.setLength(0);
        tag.emit(stringBuilder);
        Assert.assertEquals(stringBuilder.toString(),line2+"\r\n");
    }
}
