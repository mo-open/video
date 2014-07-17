package org.mds.video.hls.model.tags;

import java.util.Map;

/**
 * @author Randall.mo
 */
public class ExtSignalSpanTag extends ExtSignalAdTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTSIGNALSPAN_TAG = "#EXT-X-SIGNAL-SPAN";

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtSignalSpanTag forLine(String line) {
        if (!isLineFor(line, EXTSIGNALSPAN_TAG)) return null;
        ExtSignalSpanTag tag = new ExtSignalSpanTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTSIGNALSPAN_TAG).append(C_COLON);
        super.emit(inputOutput);
    }
}
