package org.mds.video.hls.model;

import com.google.common.io.LineReader;
import org.mds.video.hls.model.tags.ExtStreamInfTag;
import org.mds.video.hls.utils.IOUtil;
import org.mds.video.hls.utils.PlayListUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;

/**
 * @author Randall.mo
 */
public class VariantPlaylistTest {

    @Test
    public void test() throws IOException, URISyntaxException {
        VariantPlaylist variantPlaylist = null;
        Reader reader = null;
        try {
            reader = IOUtil.getReader("variantplaylist.m3u", getClass());
            variantPlaylist = VariantPlaylist.forLines("variantplaylist.m3u", reader);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        Assert.assertTrue(variantPlaylist != null);

        reader = IOUtil.getReader("variantplaylist.m3u", getClass());
        StringBuilder stringBuilder = new StringBuilder();
        LineReader lineReader = new LineReader(reader);
        String line = null;
        while (true) {
            line = lineReader.readLine();
            if (line == null) {
                break;
            }
            if ("".equals(line.trim())) continue;
            stringBuilder.append(line).append("\r\n");
        }
        reader.close();

        Assert.assertEquals(variantPlaylist.emit(),stringBuilder.toString());
        Assert.assertEquals(variantPlaylist.getVariantStreams().size(),4);
        Assert.assertEquals(variantPlaylist.getHeader().getFaxCmTag().getUri(), "1.hlsd");
        ExtStreamInfTag firstTag=variantPlaylist.getVariantStreams().get(0);
        Assert.assertEquals(firstTag.getUri(),"http://example.com/low.m3u8");
        Assert.assertEquals(firstTag.getBandwidth(),1280000);
        Assert.assertEquals(firstTag.getProgramId(),1);
        ExtStreamInfTag lastTag=variantPlaylist.getVariantStreams().get(3);
        Assert.assertEquals(lastTag.getUri(),"http://example.com/audio-only.m3u8");
        Assert.assertEquals(lastTag.getBandwidth(),65000);
        Assert.assertEquals(lastTag.getProgramId(),1);
        Assert.assertEquals(lastTag.getCodecs(),"mp4a.40.5,video/3gpp");
        Assert.assertEquals(lastTag.getResolution(),"640x480");
        PlayListUtils.remapResourceUris(variantPlaylist, "http://example.com", "http://target.com");
        Assert.assertEquals(variantPlaylist.getHeader().getFaxCmTag().getUri(), "http://target.com/1.hlsd");
        for (ExtStreamInfTag streamInfTag : variantPlaylist.getVariantStreams()) {
            Assert.assertEquals(streamInfTag.getUri().startsWith("http://target.com"), true);
        }
    }
}
