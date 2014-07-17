package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The EXT-X-STREAM-INF tag identifies a media URI as a Playlist file
 * containing a multimedia presentation and provides information about
 * that presentation.  It applies only to the URI that follows it.  Its
 * format is:
 * <p/>
 * #EXT-X-STREAM-INF:<attribute-list>
 * <URI>
 * <p/>
 * The following attributes are defined:
 * (1) BANDWIDTH
 * The value is a decimal-integer of bits per second.  It MUST be an
 * upper bound of the overall bitrate of each media segment (calculated
 * to include container overhead) that appears or will appear in the
 * Playlist.
 * Every EXT-X-STREAM-INF tag MUST include the BANDWIDTH attribute.
 * (2) PROGRAM-ID
 * The value is a decimal-integer that uniquely identifies a particular
 * presentation within the scope of the Playlist file.
 * A Playlist file MAY contain multiple EXT-X-STREAM-INF tags with the
 * same PROGRAM-ID to identify different encodings of the same
 * presentation.  These variant playlists MAY contain additional EXT-X-
 * STREAM-INF tags.
 * (3) CODECS
 * The value is a quoted-string containing a comma-separated list of
 * formats, where each format specifies a media sample type that is
 * present in a media segment in the Playlist file.  Valid format
 * identifiers are those in the ISO File Format Name Space defined by
 * RFC 6381 [RFC6381].
 * Every EXT-X-STREAM-INF tag SHOULD include a CODECS attribute.
 * (4) RESOLUTION
 * The value is a decimal-resolution describing the approximate encoded
 * horizontal and vertical resolution of video within the presentation.
 * (5) AUDIO
 * The value is a quoted-string.  It MUST match the value of the
 * GROUP-ID attribute of an EXT-X-MEDIA tag elsewhere in the Playlist
 * whose TYPE attribute is AUDIO.  It indicates the set of audio
 * renditions that MAY be used when playing the presentation.
 * (6) VIDEO
 * The value is a quoted-string.  It MUST match the value of the
 * GROUP-ID attribute of an EXT-X-MEDIA tag elsewhere in the Playlist
 * whose TYPE attribute is VIDEO.  It indicates the set of video
 * renditions that MAY be used when playing the presentation.
 * <p/>
 * When an EXT-X-STREAM-INF tag contains an AUDIO or a VIDEO attribute,
 * it indicates that alternative renditions of the content are available
 * for playback of that variant.
 * <p/>
 * When defining alternative renditions, the following constraints MUST
 * be met:
 * o  All playable combinations of renditions associated with an EXT-X-
 * STREAM-INF tag MUST have an aggregate bandwidth less than or equal
 * to the BANDWIDTH attribute of the EXT-X-STREAM-INF tag.
 * o  If an EXT-X-STREAM-INF tag contains a RESOLUTION attribute and a
 * VIDEO attribute, then every alternative video rendition MUST match
 * the value of the RESOLUTION attribute.
 * o  Every alternative rendition associated with an EXT-X-STREAM-INF
 * tag MUST meet the constraints for a variant stream.
 * The URI attribute of an EXT-X-MEDIA tag is optional.  If it is
 * missing, it indicates that the rendition described by the EXT-X-MEDIA
 * tag is present in the main Playlist described by the EXT-X-STREAM-INF
 * tag.
 * Note that if a client chooses to play renditions of audio and video
 * that are not present in the main Playlist described by the EXT-X-
 * STREAM-INF tag, or if the client chooses to play an audio rendition
 * and the main Playlist is audio-only, then the client MAY ignore the
 * main Playlist and its media.
 *
 * @author Randall.mo
 */
public class ExtStreamInfTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTSTREAMINF_TAG = "#EXT-X-STREAM-INF";
    public static String ATTR_BANDWIDTH = "BANDWIDTH";
    public static String ATTR_PROGRAMID = "PROGRAM-ID";
    public static String ATTR_CODECS = "CODECS";
    public static String ATTR_RESOLUTION = "RESOLUTION";
    public static String ATTR_AUDIO = "AUDIO";
    public static String ATTR_VIDEO = "VIDEO";

    protected int bandwidth;
    protected Integer programId = null;
    protected String codecs;
    protected String resolution;
    protected String audio;
    protected String video;
    protected String uri;

    protected static boolean setAttribute(ExtStreamInfTag tag, String attributeName, String value) {
        if (ATTR_BANDWIDTH.equals(attributeName)) {
            if (!StringUtils.isEmpty(value)) {
                try {
                    tag.bandwidth = Integer.parseInt(value);
                } catch (Exception ex) {
                    log.warn("Invalid bandwidth value: " + value);
                }
            }
            return true;
        }

        if (ATTR_PROGRAMID.equals(attributeName)) {
            if (!StringUtils.isEmpty(value)) {
                try {
                    tag.programId = Integer.parseInt(value);
                } catch (Exception ex) {
                    log.warn("Invalid programId value: " + value);
                }
            }
            return true;
        }

        if (ATTR_CODECS.equals(attributeName)) {
            tag.codecs = StringUtils.remove(value, C_DQUOTE);
            return true;
        }

        if (ATTR_RESOLUTION.equals(attributeName)) {
            tag.resolution = value;
            return true;
        }

        if (ATTR_AUDIO.equals(attributeName)) {
            tag.audio = StringUtils.remove(value, C_DQUOTE);
            return true;
        }

        if (ATTR_VIDEO.equals(attributeName)) {
            tag.video = StringUtils.remove(value, C_DQUOTE);
            return true;
        }

        return false;
    }

    private final static AttributeHandler<ExtStreamInfTag> attributeHandler = new AttributeHandler<ExtStreamInfTag>() {
        @Override
        public void findAttribute(ExtStreamInfTag tag, String attributeName, String value) {
            setAttribute(tag, attributeName, value);
        }
    };

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtStreamInfTag forLine(String line) {
        if (!isLineFor(line, EXTSTREAMINF_TAG)) return null;

        ExtStreamInfTag tag = new ExtStreamInfTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public ExtStreamInfTag setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
        return this;
    }

    public int getProgramId() {
        return programId;
    }

    public ExtStreamInfTag setProgramId(int programId) {
        this.programId = programId;
        return this;
    }

    public String getCodecs() {
        return codecs;
    }

    public ExtStreamInfTag setCodecs(String codecs) {
        this.codecs = codecs;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public ExtStreamInfTag setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public String getAudio() {
        return audio;
    }

    public ExtStreamInfTag setAudio(String audio) {
        this.audio = audio;
        return this;
    }

    public String getVideo() {
        return video;
    }

    public ExtStreamInfTag setVideo(String video) {
        this.video = video;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public ExtStreamInfTag setUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTSTREAMINF_TAG).append(C_COLON);
        if (this.programId != null) {
            inputOutput.append(ATTR_PROGRAMID).append(C_EQUAL).append(this.programId).append(C_COMMA);
        }

        inputOutput.append(ATTR_BANDWIDTH).append(C_EQUAL).append(this.bandwidth);
        if (this.codecs != null) {
            inputOutput.append(C_COMMA).append(ATTR_CODECS).append(C_EQUAL).append(C_DQUOTE).append(this.codecs).append(C_DQUOTE);
        }
        if (this.resolution != null) {
            inputOutput.append(C_COMMA).append(ATTR_RESOLUTION).append(C_EQUAL).append(this.resolution);
        }
        if (this.audio != null) {
            inputOutput.append(C_COMMA).append(ATTR_AUDIO).append(C_EQUAL).append(C_DQUOTE).append(this.audio).append(C_DQUOTE);
        }
        if (this.video != null) {
            inputOutput.append(C_COMMA).append(ATTR_VIDEO).append(C_EQUAL).append(C_DQUOTE).append(this.video).append(C_DQUOTE);
        }
        inputOutput.append(S_CRLF);
        if (this.uri != null) {
            inputOutput.append(this.uri).append(S_CRLF);
        }
    }
}
