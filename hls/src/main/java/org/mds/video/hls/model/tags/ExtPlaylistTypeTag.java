package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

/**
 * The EXT-X-PLAYLIST-TYPE tag provides mutability information about the
 * Playlist file.  It applies to the entire Playlist file.  It is
 * optional.  Its format is:
 * <p/>
 * #EXT-X-PLAYLIST-TYPE:<EVENT|VOD>
 *
 * @author Randall.mo
 */
public class ExtPlaylistTypeTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTPLAYLISTTYPE_TAG = "#EXT-X-PLAYLIST-TYPE";
    public static String OPTION__VOD = "VOD";
    public static String OPTION_EVENT = "EVENT";

    private String playlistType;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtPlaylistTypeTag forLine(String line) {
        if (!isLineFor(line, EXTPLAYLISTTYPE_TAG)) return null;

        ExtPlaylistTypeTag tag = new ExtPlaylistTypeTag();
        tag.playlistType = StringUtils.substringAfter(line, C_COLON);
        return tag;
    }

    public String getPlaylistType() {
        return playlistType;
    }

    public ExtPlaylistTypeTag setPlaylistType(String playlistType) {
        this.playlistType = playlistType;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTPLAYLISTTYPE_TAG).append(C_COLON)
                .append(playlistType).append(S_CRLF);
    }
}
