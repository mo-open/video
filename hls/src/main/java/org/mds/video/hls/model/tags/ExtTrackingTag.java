package org.mds.video.hls.model.tags;

import java.util.HashMap;
import java.util.Map;

import org.mds.video.hls.utils.StringUtils;

/**
 * Created by Randall.mo on 14-4-25.
 */
public class ExtTrackingTag extends M3uTag {
    public static String EXTTRACKING_TAG = "#EXT-X-CUE";

    public static String ATTR_TRACKING_ID = "ID";
    public static String ATTR_DURATION = "DURATION";
    public static String ATTR_PSN = "PSN";

    private String trackingId;
    private String duration;
    private String psn;

    private final static AttributeHandler<ExtTrackingTag> attributeHandler =
            new AttributeHandler<ExtTrackingTag>() {
                @Override
                public void findAttribute(ExtTrackingTag tag, String attributeName, String value) {
                    if (ATTR_TRACKING_ID.equals(attributeName)) {
                        tag.trackingId = value;
                        return;
                    }
                    if (ATTR_DURATION.equals(attributeName)) {
                        tag.duration = value;
                        return;
                    }
                    if (ATTR_PSN.equals(attributeName)) {
                        tag.psn = value;
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

    public static ExtTrackingTag forLine(String line) {
        if (!isLineFor(line, EXTTRACKING_TAG)) return null;
        ExtTrackingTag tag = new ExtTrackingTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public ExtTrackingTag setTrackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public ExtTrackingTag setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getPsn() {
        return psn;
    }

    public ExtTrackingTag setPsn(String psn) {
        this.psn = psn;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        if (StringUtils.isEmpty(trackingId) && StringUtils.isEmpty(duration) && StringUtils.isEmpty(psn)) {
            inputOutput.append(EXTTRACKING_TAG).append(S_CRLF);
        } else {
            inputOutput.append(EXTTRACKING_TAG).append(C_COLON)
                    .append(ATTR_TRACKING_ID).append(C_EQUAL)
                    .append(StringUtils.trimToEmpty(this.trackingId)).append(C_COMMA)
                    .append(ATTR_DURATION).append(C_EQUAL)
                    .append(StringUtils.trimToEmpty(this.duration)).append(C_COMMA)
                    .append(ATTR_PSN).append(C_EQUAL)
                    .append(StringUtils.trimToEmpty(this.psn))
                    .append(S_CRLF);
        }
    }
}