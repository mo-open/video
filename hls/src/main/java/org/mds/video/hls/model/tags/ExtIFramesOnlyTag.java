package org.mds.video.hls.model.tags;

/**
 * The EXT-X-I-FRAMES-ONLY tag indicates that each media segment in the
 * Playlist describes a single I-frame.  I-frames (or Intra frames) are
 * encoded video frames whose encoding does not depend on any other
 * frame.
 * The EXT-X-I-FRAMES-ONLY tag applies to the entire Playlist.  Its
 * format is:
 * #EXT-X-I-FRAMES-ONLY
 * <p/>
 * In a Playlist with the EXT-X-I-FRAMES-ONLY tag, the media segment
 * duration (EXTINF tag value) is the time between the presentation time
 * of the I-frame in the media segment and the presentation time of the
 * next I-frame in the Playlist, or the end of the presentation if it is
 * the last I-frame in the Playlist.
 * <p/>
 * Media resources containing I-frame segments MUST begin with a
 * Transport Stream PAT/PMT.  The byte range of an I-frame segment with
 * an EXT-X-BYTERANGE tag applied to it MUST NOT include
 * a PAT/PMT.
 * <p/>
 * The EXT-X-I-FRAMES-ONLY tag appeared in version 4 of the protocol.
 *
 * @author Randall.mo
 */
public class ExtIFramesOnlyTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTIFRAMESONLY_TAG = "#EXT-X-I-FRAMES-ONLY";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtIFramesOnlyTag forLine(String line) {
        if (!isLineFor(line, EXTIFRAMESONLY_TAG)) return null;
        return new ExtIFramesOnlyTag();
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTIFRAMESONLY_TAG).append(S_CRLF);
    }
}
