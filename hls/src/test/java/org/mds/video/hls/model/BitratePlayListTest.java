package org.mds.video.hls.model;

import com.google.common.io.LineReader;
import org.mds.video.hls.model.tags.composite.RecordMarker;
import org.mds.video.hls.utils.IOUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Randall.mo
 */
public class BitratePlayListTest {
    @Test
    public void testWithFullTags() throws IOException {
        BitratePlaylist bitratePlaylist = null;
        Reader reader = null;
        try {
            reader = IOUtil.getReader("biterateplaylist1.m3u", getClass());
            bitratePlaylist = BitratePlaylist.forLines("biterateplaylist1.m3u8", reader);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        reader = IOUtil.getReader("biterateplaylist1.m3u", getClass());
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
        Assert.assertTrue(bitratePlaylist != null);

        //the emit string may be not equal the original string
        //because here the input m3u has the same tags' order with the playlist implementaion
        Assert.assertEquals(bitratePlaylist.emit(), stringBuilder.toString());

        Assert.assertEquals(bitratePlaylist.getRecordMarkers().size(), 7);
        Assert.assertEquals(bitratePlaylist.getHeader().getMediaSequenceTag().getMediaSequence(), 85737);
        Assert.assertEquals(bitratePlaylist.getHeader().getTargetDurationTag().getTargetDuration(), 7);
        RecordMarker firstRecordMarker = bitratePlaylist.getRecordMarkers().get(0);
        Assert.assertTrue(firstRecordMarker.getReplaceSignalTag() != null);
        Assert.assertEquals(firstRecordMarker.getInfTag().getUri(), "20120808T073222-02-85737.ts");
        Assert.assertEquals(firstRecordMarker.getReplaceSignalId(), "BLACKOUT:1482");
        Assert.assertEquals(firstRecordMarker.getReplaceSignalTag().getDuration(), "7");
        Assert.assertNull(firstRecordMarker.getAdSignalTag());

        RecordMarker midRecordMarker = bitratePlaylist.getRecordMarkers().get(2);
        Assert.assertFalse(midRecordMarker.getAdSignalTag().isBlackout());

        RecordMarker lastRecordMarker = bitratePlaylist.getRecordMarkers().get(6);
        Assert.assertTrue(lastRecordMarker.getAdSignalTag() == null);
        Assert.assertTrue(lastRecordMarker.getReplaceSignalTag() != null);
        Assert.assertEquals(lastRecordMarker.getReplaceSignalId(), "1483");
        Assert.assertEquals(lastRecordMarker.getInfTag().getUri(), "20120808T073222-02-85743.ts");
        Assert.assertEquals(lastRecordMarker.getReplaceSignalTag().getDuration(), "3");

        BitratePlaylist bitratePlaylist1 = BitratePlaylist.forLines("biterateplaylist1.m3u", new StringReader(bitratePlaylist.emit()));
        Assert.assertEquals(bitratePlaylist.emit(), bitratePlaylist1.emit());
    }

    @Test
    public void testWithTracking() throws IOException {
        BitratePlaylist bitratePlaylist = null;
        Reader reader = null;
        try {
            reader = IOUtil.getReader("biterateplaylist-with-tracking.m3u", getClass());
            bitratePlaylist = BitratePlaylist.forLines("biterateplaylist-with-tracking.m3u8", reader);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        reader = IOUtil.getReader("biterateplaylist-with-tracking.m3u", getClass());
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
        Assert.assertTrue(bitratePlaylist != null);

        //the emit string may be not equal the original string
        //because here the input m3u has the same tags' order with the playlist implementaion
        Assert.assertEquals(bitratePlaylist.emit(), stringBuilder.toString());

        Assert.assertEquals(bitratePlaylist.getRecordMarkers().size(), 7);
        Assert.assertEquals(bitratePlaylist.getHeader().getMediaSequenceTag().getMediaSequence(), 85737);
        Assert.assertEquals(bitratePlaylist.getHeader().getTargetDurationTag().getTargetDuration(), 7);
        RecordMarker firstRecordMarker = bitratePlaylist.getRecordMarkers().get(0);
        Assert.assertNotNull(firstRecordMarker.getTrackingTag());
        Assert.assertTrue(firstRecordMarker.getReplaceSignalTag() != null);
        Assert.assertTrue(firstRecordMarker.getAdSignalTag() == null);
        Assert.assertEquals(firstRecordMarker.getInfTag().getUri(), "20120808T073222-02-85737.ts");
        Assert.assertEquals(firstRecordMarker.getReplaceSignalId(), "BLACKOUT:1482");
        Assert.assertEquals(firstRecordMarker.getReplaceSignalTag().getDuration(), "7");

        RecordMarker midRecordMarker = bitratePlaylist.getRecordMarkers().get(2);
        Assert.assertFalse(midRecordMarker.getAdSignalTag().isBlackout());

        RecordMarker lastRecordMarker = bitratePlaylist.getRecordMarkers().get(6);
        Assert.assertTrue(lastRecordMarker.getAdSignalTag() == null);
        Assert.assertTrue(lastRecordMarker.getReplaceSignalTag() != null);
        Assert.assertEquals(lastRecordMarker.getReplaceSignalId(), "1483");
        Assert.assertEquals(lastRecordMarker.getInfTag().getUri(), "20120808T073222-02-85743.ts");
        Assert.assertEquals(lastRecordMarker.getReplaceSignalTag().getDuration(), "3");

        BitratePlaylist bitratePlaylist1 = BitratePlaylist.forLines("biterateplaylist-with-tracking.m3u8", new StringReader(bitratePlaylist.emit()));
        Assert.assertEquals(bitratePlaylist.emit(), bitratePlaylist1.emit());
    }

    @Test
    public void testLongList() throws IOException {
        BitratePlaylist bitratePlaylist = null;
        Reader reader = null;
        try {
            reader = IOUtil.getReader("biterateplaylist2.m3u", getClass());
            bitratePlaylist = BitratePlaylist.forLines("biterateplaylist2.m3u8", reader);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        reader = IOUtil.getReader("biterateplaylist2.m3u", getClass());
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
        Assert.assertTrue(bitratePlaylist != null);

        //the emit string may be not equal the original string
        //because here the input m3u has the same tags' order with the playlist implementaion
        Assert.assertEquals(bitratePlaylist.emit(), stringBuilder.toString());

        Assert.assertEquals(bitratePlaylist.getRecordMarkers().size(), 62);
        Assert.assertEquals(bitratePlaylist.getHeader().getMediaSequenceTag().getMediaSequence(), 1);
        Assert.assertEquals(bitratePlaylist.getHeader().getTargetDurationTag().getTargetDuration(), 6);
        RecordMarker firstRecordMarker = bitratePlaylist.getRecordMarkers().get(0);
        Assert.assertFalse(firstRecordMarker.getAdSignalTag().isBlackout());
        Assert.assertEquals(firstRecordMarker.getReplaceSignalTag().getSignalId(), "0");
        Assert.assertEquals(firstRecordMarker.getInfTag().getUri(), "ESPN_med_01.ts");
        Assert.assertEquals(firstRecordMarker.getAdSignalTag().getSignalId(), "0");
        Assert.assertNull(firstRecordMarker.getAdSignalTag().getDuration());

        RecordMarker midRecordMarker = bitratePlaylist.getRecordMarkers().get(12);
        Assert.assertNull(midRecordMarker.getAdSignalTag());

        RecordMarker lastRecordMarker = bitratePlaylist.getRecordMarkers().get(61);
        Assert.assertFalse(lastRecordMarker.getAdSignalTag().isBlackout());
        Assert.assertEquals(lastRecordMarker.getInfTag().getUri(), "ESPN_med_62.ts");
        Assert.assertEquals(lastRecordMarker.getAdSignalTag().getSignalId(), "2");
        Assert.assertNull(lastRecordMarker.getAdSignalTag().getDuration());

        BitratePlaylist bitratePlaylist1 = BitratePlaylist.forLines("biterateplaylist1.m3u", new StringReader(bitratePlaylist.emit()));
        Assert.assertEquals(bitratePlaylist.emit(), bitratePlaylist1.emit());
    }

    @Test
    public void testClone() throws IOException {
        BitratePlaylist bitratePlaylist = null;
        Reader reader = null;
        try {
            reader = IOUtil.getReader("biterateplaylist1.m3u", getClass());
            bitratePlaylist = BitratePlaylist.forLines("biterateplaylist1.m3u8", reader);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        String originPlaylist = bitratePlaylist.emit();
        BitratePlaylist clonePlaylist = bitratePlaylist.clone();
        Assert.assertEquals(originPlaylist, clonePlaylist.emit());
    }
}
