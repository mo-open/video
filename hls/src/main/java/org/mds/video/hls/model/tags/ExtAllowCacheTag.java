package org.mds.video.hls.model.tags;

import org.mds.video.hls.utils.StringUtils;

/**
 * The EXT-X-ALLOW-CACHE tag indicates whether the client MAY or MUST
 * NOT cache downloaded media segments for later replay.  It MAY occur
 * anywhere in the Playlist file; it MUST NOT occur more than once.  The
 * EXT-X-ALLOW-CACHE tag applies to all segments in the playlist.  Its
 * format is:
 * <p/>
 * #EXT-X-ALLOW-CACHE:<YES|NO>
 *
 * @author Randall.mo
 */
public class ExtAllowCacheTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTALLOWCACHE_TAG = "#EXT-X-ALLOW-CACHE";
    public static String OPTION_YES = "YES";
    public static String OPTION_NO = "NO";

    private boolean allowCache;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtAllowCacheTag forLine(String line) {
        if (!isLineFor(line, EXTALLOWCACHE_TAG)) return null;
        ExtAllowCacheTag tag = new ExtAllowCacheTag();
        tag.allowCache = OPTION_YES.equals(StringUtils.substringAfter(line, C_COLON).trim());
        return tag;
    }

    public boolean isAllowCache() {
        return allowCache;
    }

    public ExtAllowCacheTag setAllowCache(boolean allowCache) {
        this.allowCache = allowCache;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTALLOWCACHE_TAG).append(C_COLON)
                .append(this.allowCache ? OPTION_YES : OPTION_NO).append(S_CRLF);
    }
}
