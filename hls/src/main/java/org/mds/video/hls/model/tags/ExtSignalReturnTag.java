package org.mds.video.hls.model.tags;

import java.util.Map;

/**
 * @author Randall.mo
 */
public class ExtSignalReturnTag extends ExtSignalAdTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTSIGNALRETURN_TAG = "#EXT-X-SIGNAL-RETURN";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtSignalReturnTag forLine(String line) {
        if (!isLineFor(line, EXTSIGNALRETURN_TAG)) return null;
        ExtSignalReturnTag tag = new ExtSignalReturnTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTSIGNALRETURN_TAG).append(C_COLON);
        super.emit(inputOutput);
    }
}
