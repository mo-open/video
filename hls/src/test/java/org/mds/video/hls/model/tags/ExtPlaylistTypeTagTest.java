package org.mds.video.hls.model.tags;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Randall.mo
 */
public class ExtPlaylistTypeTagTest {

    @Test
    public void test() {
        ExtPlaylistTypeTag tag = ExtPlaylistTypeTag.forLine(null);
        Assert.assertTrue(tag == null);
        String line = ExtPlaylistTypeTag.EXTPLAYLISTTYPE_TAG+":VOD";
        tag = ExtPlaylistTypeTag.forLine(line);
        Assert.assertTrue(tag != null);
        Assert.assertEquals(tag.getPlaylistType(), "VOD");
        StringBuilder stringBuilder = new StringBuilder();
        tag.emit(stringBuilder);
        Assert.assertEquals(line + "\r\n", stringBuilder.toString());
    }
}
