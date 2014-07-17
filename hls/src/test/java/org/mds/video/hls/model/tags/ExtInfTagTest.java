package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtInfTagTest {

    @Test
    public void test() {
        ExtInfTag tag = ExtInfTag.forLine(null);
        Assert.assertTrue(tag == null);

        String line = ExtInfTag.EXTINF_TAG+":6.0,test movie";
        tag = ExtInfTag.forLine(line);
        tag.setUri("test.ts");
        Assert.assertEquals(tag.getDuration(), 6.0);
        Assert.assertEquals(tag.getTitle(), "test movie");
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line+"\r\ntest.ts\r\n", stringBuilder.toString());
    }
}
