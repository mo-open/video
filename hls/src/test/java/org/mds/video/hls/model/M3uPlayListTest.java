package org.mds.video.hls.model;

import org.mds.video.hls.utils.IOUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.Reader;

/**
 * @author Randall.mo
 */
public class M3uPlayListTest {
    @Test
    public void testForLines() throws Exception {
        M3uPlayList playList = null;
        Reader reader = null;
        try {
            reader = IOUtil.getReader("biterateplaylist1.m3u", getClass());
            playList = M3uPlayList.forLines("biterateplaylist1.m3u", reader);
            Assert.assertTrue(playList instanceof BitratePlaylist);
            reader.close();
            reader = IOUtil.getReader("variantplaylist.m3u", getClass());
            playList = M3uPlayList.forLines("variantplaylist.m3u", reader);
            Assert.assertTrue(playList instanceof VariantPlaylist);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
