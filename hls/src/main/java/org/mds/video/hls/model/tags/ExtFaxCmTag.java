package org.mds.video.hls.model.tags;

import org.mds.video.hls.utils.StringUtils;

/**
 * @author Randall.mo
 */
public class ExtFaxCmTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTFAXCM_TAG = "#EXT-X-FAXS-CM";
    public static String ATTR_URI = "URI";
    private String uri;
    private String content;
    private boolean forInternal = false;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtFaxCmTag forLine(String line) {
        if (!isLineFor(line, EXTFAXCM_TAG)) return null;
        ExtFaxCmTag extFaxCmTag = new ExtFaxCmTag();
        String value = StringUtils.substringAfter(line, C_COLON);
        if (value.startsWith(ATTR_URI))
            extFaxCmTag.uri = StringUtils.remove(StringUtils.substringAfter(value, C_EQUAL), C_DQUOTE);
        else {
            extFaxCmTag.content = value;
            extFaxCmTag.forInternal = true;
        }
        return extFaxCmTag;
    }

    public String getUri() {
        return uri;
    }

    public ExtFaxCmTag setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ExtFaxCmTag setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isForInternal() {
        return forInternal;
    }

    public ExtFaxCmTag setForInternal(boolean forInternal) {
        this.forInternal = forInternal;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTFAXCM_TAG).append(C_COLON);
        if (this.content != null && (this.isForInternal() || this.uri == null)) {
            inputOutput.append(this.content).append(S_CRLF);
            return;
        }
        inputOutput.append(ATTR_URI).append(C_EQUAL).
                append(C_DQUOTE).append(this.uri).append(C_DQUOTE).append(S_CRLF);
    }
}
