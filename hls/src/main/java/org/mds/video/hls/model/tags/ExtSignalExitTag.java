package org.mds.video.hls.model.tags;

import java.util.Map;

/**
 * @author Randall.mo
 */
public class ExtSignalExitTag extends ExtSignalAdTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTSIGNALEXIT_TAG = "#EXT-X-SIGNAL-EXIT";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtSignalExitTag forLine(String line) {
        if (!isLineFor(line, EXTSIGNALEXIT_TAG)) return null;
        ExtSignalExitTag tag = new ExtSignalExitTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTSIGNALEXIT_TAG).append(C_COLON);
        super.emit(inputOutput);
    }
}
