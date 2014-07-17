package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtMediaSequenceTagTest {

    @Test
    public void test() {
        ExtMediaSequenceTag tag = ExtMediaSequenceTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtMediaSequenceTag.EXTMEDIASEQUENCE_TAG+":1";
        tag = ExtMediaSequenceTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getMediaSequence(), 1);
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
