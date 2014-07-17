package org.mds.video.hls.model.tags;

import org.mds.video.hls.utils.StringUtils;

/**
 * The EXT-X-TARGETDURATION tag specifies the maximum media segment
 * duration.  The EXTINF duration of each media segment in the Playlist
 * file MUST be less than or equal to the target duration.  This tag
 * MUST appear once in the Playlist file.  It applies to the entire
 * Playlist file.  Its format is:
 * <p/>
 * #EXT-X-TARGETDURATION:<s>
 * <p/>
 * where s is an integer indicating the target duration in seconds.
 *
 * @author Randall.mo
 */

public class ExtTargetDurationTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTTARGETDURATION_TAG = "#EXT-X-TARGETDURATION";

    private int targetDuration;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtTargetDurationTag forLine(String line) {
        if (!isLineFor(line, EXTTARGETDURATION_TAG)) return null;
        ExtTargetDurationTag tag = new ExtTargetDurationTag();
        tag.targetDuration = Integer.parseInt(StringUtils.substringAfter(line, C_COLON));
        return tag;
    }

    public int getTargetDuration() {
        return targetDuration;
    }

    public ExtTargetDurationTag setTargetDuration(int targetDuration) {
        this.targetDuration = targetDuration;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTTARGETDURATION_TAG).append(C_COLON)
                .append(targetDuration).append(S_CRLF);
    }
}
