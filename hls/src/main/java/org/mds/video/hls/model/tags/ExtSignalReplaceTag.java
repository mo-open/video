package org.mds.video.hls.model.tags;

import java.util.HashMap;
import java.util.Map;

/**
 * Blackout signal tag
 *
 * @author Randall.mo
 */
public class ExtSignalReplaceTag extends ExtSignalTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTSIGNALREPLACE_TAG = "#EXT-X-REPLACE";
    public static String ATTR_SIGNALID = "SignalID";
    public static String ATTR_DURATION = "Duration";

    protected final static AttributeHandler<ExtSignalReplaceTag> attributeHandler =
            new AttributeHandler<ExtSignalReplaceTag>() {
                @Override
                public void findAttribute(ExtSignalReplaceTag tag, String attributeName, String value) {
                    if (ATTR_SIGNALID.equals(attributeName)) {
                        tag.signalId = value;
                        return;
                    }
                    if (ATTR_DURATION.equals(attributeName)) {
                        tag.duration = value;
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

    public static ExtSignalReplaceTag forLine(String line) {
        if (!isLineFor(line, EXTSIGNALREPLACE_TAG)) return null;
        ExtSignalReplaceTag tag = new ExtSignalReplaceTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    public boolean isBlackout() {
        return true;
    }

    public ExtSignalReplaceTag setBlackout(boolean blackout) {
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTSIGNALREPLACE_TAG).append(C_COLON);
        if (this.signalId != null)
            inputOutput.append(ATTR_SIGNALID).append(C_EQUAL).append(this.signalId);

        if (this.duration != null) {
            if (this.signalId != null)
                inputOutput.append(C_COMMA);
            inputOutput.append(ATTR_DURATION).append(C_EQUAL)
                    .append(this.duration);
        }

        inputOutput.append(S_CRLF);
    }
}
