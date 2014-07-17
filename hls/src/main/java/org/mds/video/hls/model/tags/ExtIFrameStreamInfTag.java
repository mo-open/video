package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The EXT-X-I-FRAME-STREAM-INF tag identifies a Playlist file
 * containing the I-frames of a multimedia presentation.  It stands
 * alone, in that it does not apply to a particular URI in the Playlist.
 * Its format is:
 * <p/>
 * #EXT-X-I-FRAME-STREAM-INF:<attribute-list>
 * All attributes defined for the EXT-X-STREAM-INF tag
 * are also defined for the EXT-X-I-FRAME-STREAM-INF tag, except for the
 * AUDIO attribute.  In addition, the following attribute is defined:
 * URI
 * The value is a quoted-string containing a URI that identifies the
 * I-frame Playlist file.
 * <p/>
 * Every EXT-X-I-FRAME-STREAM-INF tag MUST include a BANDWIDTH attribute
 * and a URI attribute.
 * <p/>
 * The provisions in EXT-X-STREAM-INF also apply to EXT-X-I-FRAME-
 * STREAM-INF tags with a VIDEO attribute.
 * <p/>
 * A Playlist that specifies alternative VIDEO renditions and I-frame
 * Playlists SHOULD include an alternative I-frame VIDEO rendition for
 * each regular VIDEO rendition, with the same NAME and LANGUAGE
 * attributes.
 * <p/>
 * The EXT-X-I-FRAME-STREAM-INF tag appeared in version 4 of the
 * protocol.  Clients that do not implement protocol version 4 or higher
 * MUST ignore it.
 *
 * @author Randall.mo
 */
public class ExtIFrameStreamInfTag extends ExtStreamInfTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTIFRAMESTREAMINF_TAG = "#EXT-X-I-FRAME-STREAM-INF";
    public static String ATTR_URI = "URI";

    private final static AttributeHandler<ExtIFrameStreamInfTag> attributeHandler =
            new AttributeHandler<ExtIFrameStreamInfTag>() {
                @Override
                public void findAttribute(ExtIFrameStreamInfTag tag, String attributeName, String value) {
                    if (!setAttribute(tag, attributeName, value)) {
                        if (ATTR_URI.equals(attributeName)) {
                            tag.uri = StringUtils.remove(value, C_DQUOTE);
                        }
                    }
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

    public static ExtIFrameStreamInfTag forLine(String line) {
        if (!isLineFor(line, EXTIFRAMESTREAMINF_TAG)) return null;
        ExtIFrameStreamInfTag tag = new ExtIFrameStreamInfTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTIFRAMESTREAMINF_TAG).append(C_COLON);
        if (this.programId != null) {
            inputOutput.append(ExtStreamInfTag.ATTR_PROGRAMID).append(C_EQUAL).append(this.programId).append(C_COMMA);
        }

        inputOutput.append(ExtStreamInfTag.ATTR_BANDWIDTH).append(C_EQUAL).append(this.bandwidth);
        if (this.codecs != null) {
            inputOutput.append(C_COMMA).append(ExtStreamInfTag.ATTR_CODECS).append(C_EQUAL).append(C_DQUOTE).append(this.codecs).append(C_DQUOTE);
        }
        if (this.resolution != null) {
            inputOutput.append(C_COMMA).append(ExtStreamInfTag.ATTR_RESOLUTION).append(C_EQUAL).append(this.resolution);
        }
        if (this.audio != null) {
            inputOutput.append(C_COMMA).append(ATTR_AUDIO).append(C_EQUAL).append(C_DQUOTE).append(this.audio).append(C_DQUOTE);
        }
        if (this.video != null) {
            inputOutput.append(C_COMMA).append(ATTR_VIDEO).append(C_EQUAL).append(C_DQUOTE).append(this.video).append(C_DQUOTE);
        }

        inputOutput.append(C_COMMA).append(ATTR_URI).append(C_EQUAL)
                .append(C_DQUOTE).append(this.uri).append(C_DQUOTE).append(S_CRLF);
    }
}
