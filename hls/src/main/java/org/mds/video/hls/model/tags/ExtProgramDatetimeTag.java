package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

/**
 * The EXT-X-PROGRAM-DATE-TIME tag associates the first sample of a
 * media segment with an absolute date and/or time.  It applies only to
 * the next media URI.
 * <p/>
 * The date/time representation is ISO/IEC 8601:2004 [ISO_8601] and
 * SHOULD indicate a time zone:
 * <p/>
 * #EXT-X-PROGRAM-DATE-TIME:<YYYY-MM-DDThh:mm:ssZ>
 * <p/>
 * For example:
 * <p/>
 * #EXT-X-PROGRAM-DATE-TIME:2010-02-19T14:54:23.031+08:00
 *
 * @author Randall.mo
 */
public class ExtProgramDatetimeTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTPROGRAMDATETIME_TAG = "#EXT-X-PROGRAM-DATE-TIME";

    private String programDateTime;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtProgramDatetimeTag forLine(String line) {
        if (!isLineFor(line, EXTPROGRAMDATETIME_TAG)) return null;
        ExtProgramDatetimeTag tag = new ExtProgramDatetimeTag();
        tag.programDateTime = StringUtils.substringAfter(line, C_COLON);
        return tag;
    }

    public String getProgramDateTime() {
        return programDateTime;
    }

    public ExtProgramDatetimeTag setProgramDateTime(String programDateTime) {
        this.programDateTime = programDateTime;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTPROGRAMDATETIME_TAG).append(C_COLON)
                .append(this.programDateTime).append(S_CRLF);
    }
}
