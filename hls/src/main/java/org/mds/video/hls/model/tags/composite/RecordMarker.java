package org.mds.video.hls.model.tags.composite;

import com.google.common.io.LineReader;
import org.mds.video.hls.model.M3uPlayList;
import org.mds.video.hls.model.tags.*;

import java.io.IOException;

/**
 * @author Randall.mo
 */
public class RecordMarker extends CompositeTag {
    private ExtTrackingTag trackingTag;
    private ExtKeyTag keyTag;
    private ExtProgramDatetimeTag programDatetimeTag;
    private ExtInfTag infTag;
    private ExtDiscontinuityTag discontinuityTag;
    private ExtByteRangeTag byteRangeTag;
    private ExtSignalTag replaceSignalTag;
    private ExtSignalAdTag adSignalTag;

    public enum DiscontinuityLocation {
        NONE,
        BEFORE_KEY,
        BEFORE_RECORD,
        AFTER_RECORD,
    }

    private DiscontinuityLocation discontinuityLocation = null;

    private int sequenceNumber;
    private boolean segmentURIAddressedAdContent = false;

    public boolean fillFrom(M3uTag startTag,
                            LineReader lineReader) throws IOException {
        return this.fillFrom(startTag, lineReader, null);
    }

    public boolean fillFrom(M3uTag startTag,
                            LineReader lineReader,
                            M3uPlayList.UriReMapper uriReMapper) throws IOException {
        Object lineTag = startTag;
        while (true) {
            if (!fillTag(lineTag)) return false;
            if (lineTag instanceof ExtInfTag) {
                if (uriReMapper != null)
                    ((ExtInfTag) lineTag).setUri(uriReMapper.rewrite(lineReader.readLine()));
                else
                    ((ExtInfTag) lineTag).setUri(lineReader.readLine());
                return true;
            }
            lineTag = M3uPlayList.parseLine(lineReader);
        }
    }

    private boolean fillTag(Object tag) {
        if (tag instanceof ExtTrackingTag) {
            this.trackingTag = (ExtTrackingTag) tag;
            return true;
        }

        if (tag instanceof ExtKeyTag) {
            this.keyTag = (ExtKeyTag) tag;
            return true;
        }

        if (tag instanceof ExtProgramDatetimeTag) {
            this.programDatetimeTag = (ExtProgramDatetimeTag) tag;
            return true;
        }

        if (tag instanceof ExtByteRangeTag) {
            this.byteRangeTag = (ExtByteRangeTag) tag;
            return true;
        }

        if (tag instanceof ExtDiscontinuityTag) {
            this.discontinuityTag = (ExtDiscontinuityTag) tag;
            return true;
        }

        if (tag instanceof ExtSignalTag) {
            ExtSignalTag signalTag = (ExtSignalTag) tag;
            if (signalTag.isBlackout()) {
                this.replaceSignalTag = signalTag;
            } else
                this.adSignalTag = (ExtSignalAdTag) signalTag;
            return true;
        }

        if (tag instanceof ExtInfTag) {
            this.infTag = (ExtInfTag) tag;
            return true;
        }

        return false;
    }

    public ExtTrackingTag getTrackingTag() {
        return this.trackingTag;
    }

    public ExtTrackingTag getOrNewTrackingTag() {
        if (this.trackingTag == null) {
            this.trackingTag = new ExtTrackingTag();
        }
        return this.trackingTag;
    }

    public ExtKeyTag getKeyTag() {
        return keyTag;
    }

    public ExtKeyTag getOrNewKeyTag() {
        if (this.keyTag == null) {
            this.keyTag = new ExtKeyTag();
        }
        return this.keyTag;
    }

    public void setSignalTag(ExtSignalTag signalTag) {
        if (signalTag == null) {
            return;
        }

        if (signalTag.isBlackout()) {
            this.replaceSignalTag = signalTag;
            return;
        }
        this.adSignalTag = (ExtSignalAdTag) signalTag;
    }

    public void setKeyTag(ExtKeyTag keyTag) {
        this.keyTag = keyTag;
    }

    public ExtProgramDatetimeTag getProgramDatetimeTag() {
        return programDatetimeTag;
    }

    public ExtProgramDatetimeTag getOrNewProgramDatetimeTag() {
        if (this.programDatetimeTag == null) {
            this.programDatetimeTag = new ExtProgramDatetimeTag();
        }
        return programDatetimeTag;
    }

    public void setProgramDatetimeTag(ExtProgramDatetimeTag programDatetimeTag) {
        this.programDatetimeTag = programDatetimeTag;
    }

    public ExtInfTag getInfTag() {
        return infTag;
    }

    public ExtInfTag getOrNewInfTag() {
        if (this.infTag == null) {
            this.infTag = new ExtInfTag();
        }
        return infTag;
    }

    public void setInfTag(ExtInfTag infTag) {
        this.infTag = infTag;
    }

    public ExtDiscontinuityTag getDiscontinuityTag() {
        return discontinuityTag;
    }

    public ExtDiscontinuityTag getOrNewDiscontinuityTag() {
        if (this.discontinuityTag == null) {
            this.discontinuityTag = new ExtDiscontinuityTag();
        }
        return discontinuityTag;
    }

    public void setDiscontinuityTag(ExtDiscontinuityTag discontinuityTag) {
        this.discontinuityTag = discontinuityTag;
    }

    public ExtByteRangeTag getByteRangeTag() {
        return byteRangeTag;
    }

    public ExtByteRangeTag getOrNewByteRangeTag() {
        if (this.byteRangeTag == null) {
            this.byteRangeTag = new ExtByteRangeTag();
        }
        return byteRangeTag;
    }

    public void setByteRangeTag(ExtByteRangeTag byteRangeTag) {
        this.byteRangeTag = byteRangeTag;
    }

    public ExtSignalAdTag getAdSignalTag() {
        return this.adSignalTag;
    }

    public void setAdSignalTag(ExtSignalAdTag adSignalTag) {
        this.adSignalTag = adSignalTag;
    }

    public ExtSignalTag getReplaceSignalTag() {
        return replaceSignalTag;
    }

    public void setReplaceSignalTag(ExtSignalReplaceTag replaceSignalTag) {
        this.replaceSignalTag = replaceSignalTag;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public DiscontinuityLocation getDiscontinuityLocation() {
        return discontinuityLocation;
    }

    public void setDiscontinuityLocation(DiscontinuityLocation discontinuityLocation) {
        this.discontinuityLocation = discontinuityLocation;
    }

    public boolean isSegmentURIAddressedAdContent() {
        return segmentURIAddressedAdContent;
    }

    public void setSegmentURIAddressedAdContent(boolean segmentURIAddressedAdContent) {
        this.segmentURIAddressedAdContent = segmentURIAddressedAdContent;
    }

    public String getReplaceSignalId() {
        if (this.replaceSignalTag != null) {
            return this.replaceSignalTag.getSignalId();
        }
        if (this.adSignalTag != null && this.adSignalTag.isBlackout()) {
            return this.adSignalTag.getSignalId();
        }
        return null;
    }

    public void setTrackingTag(ExtTrackingTag trackingTag) {
        this.trackingTag = trackingTag;
    }

    public void emit(StringBuilder inOut) {
        if (this.trackingTag != null) this.trackingTag.emit(inOut);
        if (this.keyTag != null) this.keyTag.emit(inOut);
        if (this.adSignalTag != null) this.adSignalTag.emit(inOut);
        if (this.replaceSignalTag != null) this.replaceSignalTag.emit(inOut);
        if (this.byteRangeTag != null) this.byteRangeTag.emit(inOut);
        if (this.programDatetimeTag != null) this.programDatetimeTag.emit(inOut);
        if (this.discontinuityTag != null) this.discontinuityTag.emit(inOut);
        if (this.infTag != null) this.infTag.emit(inOut);
    }

    public RecordMarker clone() {
        RecordMarker recordMarker = (RecordMarker) super.clone();

        //About the order of discontinuity tag and the key tag:
        //the key is to affect the encryption of the following media uri.
        //the discontinuity is not relative to the key. So the order of them should be not important.
        if (this.trackingTag != null) recordMarker.trackingTag = (ExtTrackingTag) this.trackingTag.clone();
        if (this.keyTag != null) recordMarker.keyTag = (ExtKeyTag) this.keyTag.clone();
        if (this.adSignalTag != null) recordMarker.adSignalTag = (ExtSignalAdTag) this.adSignalTag.clone();
        if (this.replaceSignalTag != null)
            recordMarker.replaceSignalTag = (ExtSignalTag) this.replaceSignalTag.clone();
        if (this.byteRangeTag != null) recordMarker.byteRangeTag = (ExtByteRangeTag) this.byteRangeTag.clone();
        if (this.programDatetimeTag != null)
            recordMarker.programDatetimeTag = (ExtProgramDatetimeTag) this.programDatetimeTag.clone();
        if (this.discontinuityTag != null)
            recordMarker.discontinuityTag = (ExtDiscontinuityTag) this.discontinuityTag.clone();
        if (this.infTag != null) recordMarker.infTag = (ExtInfTag) this.infTag.clone();

        return recordMarker;
    }
}
