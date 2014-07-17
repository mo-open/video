package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

/**
 * The EXTINF tag specifies the duration of a media segment.  It applies
 * only to the media URI that follows it.  Each media segment URI MUST
 * be preceded by an EXTINF tag.  Its format is:
 * <p/>
 * #EXTINF:<duration>,<title>
 * "duration" is an integer or floating-point number in decimal
 * positional notation that specifies the duration of the media segment
 * in seconds.  Durations that are reported as integers SHOULD be
 * rounded to the nearest integer.  Durations MUST be integers if the
 * protocol version of the Playlist file is less than 3.  The remainder
 * of the line following the comma is an optional human-readable
 * informative title of the media segment.
 *
 * @author Randall.mo
 */
public class ExtInfTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTINF_TAG = "#EXTINF";
    private String origin;
    private boolean parsed = false;
    private double duration;
    private String title;
    private String uri;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtInfTag forLine(String line) {
        if (!isLineFor(line, EXTINF_TAG)) return null;
        ExtInfTag tag = new ExtInfTag();
        tag.origin = line.trim();
        return tag;
    }

    private void parse() {
        if (this.parsed) return;
        if (this.origin == null) {
            this.parsed = true;
            return;
        }
        String line = StringUtils.substringAfter(this.origin, C_COLON);
        int indexComma = line.indexOf(C_COMMA);
        if (indexComma == -1) {
            this.duration = Double.parseDouble(line);
        } else {
            String duration = line.substring(0, indexComma);
            this.duration = Double.parseDouble(duration.trim());
            this.title = line.substring(indexComma + 1).trim();
        }
        this.parsed = true;
    }

    public ExtInfTag setDuration(double duration) {
        this.parse();
        this.duration = duration;
        return this;
    }

    public double getDuration() {
        this.parse();
        return this.duration;
    }

    public ExtInfTag setTitle(String title) {
        this.parse();
        this.title = title;
        return this;
    }

    public String getTitle() {
        this.parse();
        return this.title;
    }

    public String getUri() {
        return uri;
    }

    public ExtInfTag setUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        if (this.parsed) {
            inputOutput.append(EXTINF_TAG).append(C_COLON)
                    .append(this.duration).append(C_COMMA);
            if (this.title != null)
                inputOutput.append(this.title);
        } else
            inputOutput.append(this.origin);

        inputOutput.append(S_CRLF).append(uri).append(S_CRLF);
    }
}
