package org.mds.video.hls.model.tags;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Randall.mo
 */
public class ExtSignalAdTag extends ExtSignalTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String ATTR_SIGNALID = "SignalID";
    public static String ATTR_DURATION = "Duration";

    public static String SIGNALID_OPTION_BLACKOUT = "BLACKOUT";

    protected boolean hasBlackoutPrefix = false;

    protected final static AttributeHandler<ExtSignalAdTag> attributeHandler =
            new AttributeHandler<ExtSignalAdTag>() {
                @Override
                public void findAttribute(ExtSignalAdTag tag, String attributeName, String value) {
                    if (ATTR_SIGNALID.equals(attributeName)) {
                        tag.signalId = value;
                        if (tag.signalId != null) {
                            tag.hasBlackoutPrefix = tag.signalId.startsWith(SIGNALID_OPTION_BLACKOUT);

                            //if (this.hasBlackoutPrefix)
                            //    this.signalId = StringUtils.substringAfter(this.signalId, C_COLON);
                        }
                        return;
                    }
                    if (ATTR_DURATION.equals(attributeName)) {
                        tag.duration = value;
                        return;
                    }
                }
            };

    public boolean isBlackout() {
        return hasBlackoutPrefix;
    }

    public ExtSignalTag setBlackout(boolean blackout) {
        hasBlackoutPrefix = blackout;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(ATTR_SIGNALID).append(C_EQUAL);
        if (this.signalId != null)
            inputOutput.append(this.signalId);

        if (this.duration != null) {
            if (this.signalId != null)
                inputOutput.append(C_COMMA);
            inputOutput.append(ATTR_DURATION).append(C_EQUAL)
                    .append(this.duration);
        }

        inputOutput.append(S_CRLF);
    }
}
