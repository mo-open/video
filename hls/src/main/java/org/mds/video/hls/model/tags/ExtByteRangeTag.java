package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

/**
 * The EXT-X-BYTERANGE tag indicates that a media segment is a sub-range
 * of the resource identified by its media URI.  It applies only to the
 * next media URI that follows it in the Playlist.  Its format is:
 * <p/>
 * #EXT-X-BYTERANGE:<n>[@o]
 * where n is a decimal-integer indicating the length of the sub-range
 * in bytes.  If present, o is a decimal-integer indicating the start of
 * the sub-range, as a byte offset from the beginning of the resource.
 * If o is not present, the sub-range begins at the next byte following
 * the sub-range of the previous media segment.
 * <p/>
 * If o is not present, a previous media segment MUST appear in the
 * Playlist file and MUST be a sub-range of the same media resource.
 * <p/>
 * A media URI with no EXT-X-BYTERANGE tag applied to it specifies a
 * media segment that consists of the entire resource.
 * <p/>
 * The EXT-X-BYTERANGE tag appeared in version 4 of the protocol.
 *
 * @author Randall.mo
 */
public class ExtByteRangeTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTBYTERANGE_TAG = "#EXT-X-BYTERANGE";

    private int length, offset;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtByteRangeTag forLine(String line) {
        if (!isLineFor(line, EXTBYTERANGE_TAG)) return null;
        ExtByteRangeTag tag = new ExtByteRangeTag();

        String[] values = StringUtils.split(StringUtils.substringAfter(line, C_COLON), C_AT);
        tag.length = Integer.parseInt(values[0]);
        if (values.length > 1) {
            tag.offset = Integer.parseInt(values[1]);
        }
        return tag;
    }


    public int getLength() {
        return length;
    }

    public ExtByteRangeTag setLength(int length) {
        this.length = length;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public ExtByteRangeTag setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTBYTERANGE_TAG).append(C_COLON)
                .append(length).append(C_AT).append(offset).append(S_CRLF);
    }
}
