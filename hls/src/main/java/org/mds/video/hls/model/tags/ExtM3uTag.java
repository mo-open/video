package org.mds.video.hls.model.tags;

import com.google.common.io.LineReader;

/**
 * An Extended M3U file is distinguished from a basic M3U file by its
 * first line, which MUST be the tag #EXTM3U.
 *
 * @author Randall.mo
 */
public class ExtM3uTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTM3U_TAG = "#EXTM3U";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtM3uTag forLine(String line) {
        if (!isLineFor(line, EXTM3U_TAG)) return null;
        return new ExtM3uTag();
    }

    public static boolean asHeadIn(LineReader lineReader) {
        try {
            String line = lineReader.readLine();
            while (line != null) {
                if ("".equals(line.trim())) {
                    line = lineReader.readLine();
                    continue;
                }
                return line.startsWith(EXTM3U_TAG);
            }
        } catch (Exception ex) {
            log.error("Failed to read line: " + ex);
        }
        return false;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTM3U_TAG).append(S_CRLF);
    }
}
