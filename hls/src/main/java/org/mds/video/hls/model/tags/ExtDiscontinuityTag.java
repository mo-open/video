package org.mds.video.hls.model.tags;

/**
 * The EXT-X-DISCONTINUITY tag indicates an encoding discontinuity
 * between the media segment that follows it and the one that preceded
 * it.  The set of characteristics that MAY change is:
 * o  file format
 * o  number and type of tracks
 * o  encoding parameters
 * o  encoding sequence
 * o  timestamp sequence
 * Its format is:
 * #EXT-X-DISCONTINUITY
 *
 * @author Randall.mo
 */
public class ExtDiscontinuityTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTDISCONTINUITY_TAG = "#EXT-X-DISCONTINUITY";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtDiscontinuityTag forLine(String line) {
        if (!isLineFor(line, EXTDISCONTINUITY_TAG)) return null;
        return new ExtDiscontinuityTag();
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTDISCONTINUITY_TAG).append(S_CRLF);
    }
}
