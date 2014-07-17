package org.mds.video.hls.model.tags;

/**
 * @author Randall.mo
 */
public class UnknownTag extends M3uTag {
    private String tagLine = null;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static UnknownTag forLine(String line) {
        UnknownTag tag = new UnknownTag();
        tag.tagLine = line;
        return tag;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        if (tagLine != null)
            inputOutput.append(tagLine).append(S_CRLF);
    }
}
