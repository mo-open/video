package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

/**
 * Each media URI in a Playlist has a unique integer sequence number.
 * The sequence number of a URI is equal to the sequence number of the
 * URI that preceded it plus one.  The EXT-X-MEDIA-SEQUENCE tag
 * indicates the sequence number of the first URI that appears in a
 * Playlist file.  Its format is:
 * <p/>
 * #EXT-X-MEDIA-SEQUENCE:<number>
 * <p/>
 * A Playlist file MUST NOT contain more than one EXT-X-MEDIA-SEQUENCE
 * tag.  If the Playlist file does not contain an EXT-X-MEDIA-SEQUENCE
 * tag then the sequence number of the first URI in the playlist SHALL
 * be considered to be 0.
 * <p/>
 * A media URI is not required to contain its sequence number.
 *
 * @author Randall.mo
 */
public class ExtMediaSequenceTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTMEDIASEQUENCE_TAG = "#EXT-X-MEDIA-SEQUENCE";

    private int mediaSequence;
    //is it useful??
    private Integer originalMediaSequence;

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtMediaSequenceTag forLine(String line) {
        if (!isLineFor(line, EXTMEDIASEQUENCE_TAG)) return null;
        ExtMediaSequenceTag tag = new ExtMediaSequenceTag();
        tag.setMediaSequence(Integer.parseInt(StringUtils.substringAfter(line, C_COLON)));
        return tag;
    }

    public int getMediaSequence() {
        return mediaSequence;
    }

    public ExtMediaSequenceTag setMediaSequence(int mediaSequence) {
        this.mediaSequence = mediaSequence;
        if (this.originalMediaSequence == null) {
            this.originalMediaSequence = new Integer(mediaSequence);
        }
        return this;
    }

    public Integer getOriginalMediaSequence() {
        return originalMediaSequence;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTMEDIASEQUENCE_TAG).append(C_COLON)
                .append(mediaSequence).append(S_CRLF);
    }
}
