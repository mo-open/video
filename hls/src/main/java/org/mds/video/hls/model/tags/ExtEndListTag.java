package org.mds.video.hls.model.tags;

/**
 * The EXT-X-ENDLIST tag indicates that no more media segments will be
 * added to the Playlist file.  It MAY occur anywhere in the Playlist
 * file; it MUST NOT occur more than once.  Its format is:
 * <p/>
 * #EXT-X-ENDLIST
 *
 * @author Randall.mo
 */
public class ExtEndListTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTENDLIST_TAG = "#EXT-X-ENDLIST";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtEndListTag forLine(String line) {
        if (!isLineFor(line, EXTENDLIST_TAG)) return null;
        return new ExtEndListTag();
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTENDLIST_TAG).append(S_CRLF);
    }
}
