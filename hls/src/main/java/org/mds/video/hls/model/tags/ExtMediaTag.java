package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The EXT-X-MEDIA tag is used to relate Playlists that contain
 * alternative renditions of the same content.  For example, three EXT-
 * X-MEDIA tags can be used to identify audio-only Playlists that
 * contain English, French and Spanish renditions of the same
 * presentation.  Or two EXT-X-MEDIA tags can be used to identify video-
 * only Playlists that show two different camera angles.
 * <p/>
 * The EXT-X-MEDIA tag stands alone, in that it does not apply to a
 * particular URI in the Playlist.  Its format is:
 * <p/>
 * #EXT-X-MEDIA:<attribute-list>
 * <p/>
 * The following attributes are defined:
 * (1) URI
 * The value is a quoted-string containing a URI that identifies the
 * Playlist file.  This attribute is optional;
 * (2) TYPE
 * The value is enumerated-string; valid strings are AUDIO and VIDEO.
 * If the value is AUDIO, the Playlist described by the tag MUST contain
 * audio media.  If the value is VIDEO, the Playlist MUST contain video
 * media.
 * (3) GROUP-ID
 * The value is a quoted-string identifying a mutually-exclusive group
 * of renditions.  The presence of this attribute signals membership in
 * the group.
 * (4) LANGUAGE
 * The value is a quoted-string containing an RFC 5646 [RFC5646]
 * language tag that identifies the primary language used in the
 * rendition.  This attribute is optional.
 * (5) NAME
 * The value is a quoted-string containing a human-readable description
 * of the rendition.  If the LANGUAGE attribute is present then this
 * description SHOULD be in that language.
 * (6) DEFAULT
 * The value is enumerated-string; valid strings are YES and NO.  If the
 * value is YES, then the client SHOULD play this rendition of the
 * content in the absence of information from the user indicating a
 * different choice.  This attribute is optional.  Its absence indicates
 * an implicit value of NO.
 * (7) AUTOSELECT
 * The value is enumerated-string; valid strings are YES and NO.  This
 * attribute is optional.  Its absence indicates an implicit value of
 * NO.  If the value is YES, then the client MAY choose to play this
 * rendition in the absence of explicit user preference because it
 * matches the current playback environment, such as chosen system
 * language.
 * <p/>
 * The EXT-X-MEDIA tag appeared in version 4 of the protocol.
 * <p/>
 * All EXT-X-MEDIA tags in a Playlist MUST meet the following
 * constraints:
 * o  All EXT-X-MEDIA tags in the same group MUST have the same TYPE
 * attribute.
 * o  All EXT-X-MEDIA tags in the same group MUST have different NAME
 * attributes.
 * o  A group MUST NOT have more than one member with a DEFAULT
 * attribute of YES.
 * o  All members of a group whose AUTOSELECT attribute has a value of
 * YES MUST have LANGUAGE [RFC5646] attributes with unique values.
 * o  All members of a group with TYPE=AUDIO MUST use the same audio
 * sample format.
 * o  All members of a group with TYPE=VIDEO MUST use the same video
 * sample format.
 *
 * @author Randall.mo
 */
public class ExtMediaTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTMEDIA_TAG = "#EXT-X-MEDIA";
    public static String ATTR_URI = "URI";
    public static String ATTR_TYPE = "TYPE";
    public static String ATTR_GROUPID = "GROUP-ID";
    public static String ATTR_LANGUAGE = "LANGUAGE";
    public static String ATTR_NAME = "NAME";
    public static String ATTR_DEFAULT = "DEFAULT";
    public static String ATTR_AUTOSELECT = "AUTOSELECT";

    public static String TYPE_OPTION_AUDIO = "AUDIO";
    public static String TYPE_OPTION_VIDEO = "VIDEO";

    public static String DEFAULT_OPTION_YES = "YES";
    public static String DEFAULT_OPTION_NO = "NO";

    public static String AUTOSELECT_OPTION_YES = "YES";
    public static String AUTOSELECT_OPTION_NO = "NO";

    private String uri;
    private String type;
    private String groupId;
    private String language;
    private String name;
    private Boolean asDefault, autoSelect;

    private static final AttributeHandler<ExtMediaTag> attributeHandler =
            new AttributeHandler<ExtMediaTag>() {
                @Override
                public void findAttribute(ExtMediaTag tag, String attributeName, String value) {
                    if (ATTR_URI.equals(attributeName)) {
                        tag.uri = StringUtils.remove(value, C_DQUOTE);
                        return;
                    }
                    if (ATTR_TYPE.equals(attributeName)) {
                        tag.type = value;
                        return;
                    }
                    if (ATTR_GROUPID.equals(attributeName)) {
                        tag.groupId = StringUtils.remove(value, C_DQUOTE);
                        return;
                    }
                    if (ATTR_LANGUAGE.equals(attributeName)) {
                        tag.language = StringUtils.remove(value, C_DQUOTE);
                        return;
                    }
                    if (ATTR_NAME.equals(attributeName)) {
                        tag.name = StringUtils.remove(value, C_DQUOTE);
                        return;
                    }
                    if (ATTR_DEFAULT.equals(attributeName)) {
                        if (value != null)
                            tag.asDefault = DEFAULT_OPTION_YES.equals(value) ? true : false;
                        return;
                    }
                    if (ATTR_AUTOSELECT.equals(attributeName)) {
                        if (value != null)
                            tag.autoSelect = DEFAULT_OPTION_YES.equals(value) ? true : false;
                        return;
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

    public static ExtMediaTag forLine(String line) {
        if (!isLineFor(line, EXTMEDIA_TAG)) return null;
        ExtMediaTag tag = new ExtMediaTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    public String getUri() {
        return uri;
    }

    public ExtMediaTag setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getType() {
        return type;
    }

    public ExtMediaTag setType(String type) {
        this.type = type;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public ExtMediaTag setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public ExtMediaTag setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExtMediaTag setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isAsDefault() {
        return asDefault;
    }

    public ExtMediaTag setAsDefault(boolean asDefault) {
        this.asDefault = asDefault;
        return this;
    }

    public boolean isAutoSelect() {
        return autoSelect;
    }

    public ExtMediaTag setAutoSelect(boolean autoSelect) {
        this.autoSelect = autoSelect;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTMEDIA_TAG).append(C_COLON);
        inputOutput.append(ATTR_TYPE).append(C_EQUAL).append(this.type).append(C_COMMA);
        inputOutput.append(ATTR_GROUPID).append(C_EQUAL).append(C_DQUOTE)
                .append(this.groupId).append(C_DQUOTE).append(C_COMMA);
        inputOutput.append(ATTR_NAME).append(C_EQUAL).append(C_DQUOTE).append(this.name).append(C_DQUOTE);

        if (this.asDefault != null) {
            inputOutput.append(C_COMMA).append(ATTR_DEFAULT).append(C_EQUAL)
                    .append(this.asDefault ? DEFAULT_OPTION_YES : DEFAULT_OPTION_NO);
        }
        if (this.autoSelect != null)
            inputOutput.append(C_COMMA).append(ATTR_AUTOSELECT).append(C_EQUAL)
                    .append(this.autoSelect ? AUTOSELECT_OPTION_YES : AUTOSELECT_OPTION_NO);
        if (!StringUtils.isEmpty(this.language)) {
            inputOutput.append(C_COMMA).append(ATTR_LANGUAGE).append(C_EQUAL).append(C_DQUOTE)
                    .append(this.language).append(C_DQUOTE);
        }
        if (!StringUtils.isEmpty(this.uri)) {
            inputOutput.append(C_COMMA).append(ATTR_URI).append(C_EQUAL)
                    .append(C_DQUOTE).append(this.uri).append(C_DQUOTE);
        }
        inputOutput.append(S_CRLF);
    }
}
