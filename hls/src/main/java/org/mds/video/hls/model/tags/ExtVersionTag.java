package org.mds.video.hls.model.tags;

import org.mds.video.hls.utils.StringUtils;

/**
 * The EXT-X-VERSION tag indicates the compatibility version of the
 * Playlist file.  The Playlist file, its associated media, and its
 * server MUST comply with all provisions of the most-recent version of
 * this document describing the protocol version indicated by the tag
 * value.
 * <p/>
 * The EXT-X-VERSION tag applies to the entire Playlist file.  Its
 * format is:
 * <p/>
 * #EXT-X-VERSION:<n>
 * <p/>
 * where n is an integer indicating the protocol version.
 * <p/>
 * A Playlist file MUST NOT contain more than one EXT-X-VERSION tag.  A
 * Playlist file that does not contain an EXT-X-VERSION tag MUST comply
 * with version 1 of this protocol.
 *
 * @author Randall.mo
 */
public class ExtVersionTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTVERSION_TAG = "#EXT-X-VERSION";

    private int version;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtVersionTag forLine(String line) {
        if (!isLineFor(line, EXTVERSION_TAG)) return null;

        ExtVersionTag tag = new ExtVersionTag();
        tag.version = Integer.parseInt(StringUtils.substringAfter(line, C_COLON));
        return tag;
    }

    public int getVersion() {
        return version;
    }

    public ExtVersionTag setVersion(int version) {
        this.version = version;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        if (this.version > 0) {
            inputOutput.append(EXTVERSION_TAG).append(C_COLON)
                    .append(version).append(S_CRLF);
        }
    }
}
