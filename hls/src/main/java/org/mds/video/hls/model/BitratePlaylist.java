package org.mds.video.hls.model;

import java.io.*;
import java.util.*;

import org.mds.video.hls.model.tags.*;
import org.mds.video.hls.model.tags.composite.*;
import org.mds.video.hls.utils.StringUtils;
import com.google.common.io.LineReader;

public class BitratePlaylist extends M3uPlayList {
    private ExtEndListTag endListTag;
    private String adPosition;
    private String tracking;
    private List<RecordMarker> recordMarkers = new ArrayList<RecordMarker>();

    //this attribute from Stream inf tag, maybe in the future, on stream inf tag field should be added in this playlist
    private int bandwidth;
    private boolean hasAlternateContent = false;

    public static BitratePlaylist forLines(String rescourceURI, Reader in, UriReMapper uriReMapper) throws IOException {
        LineReader lineReader = new LineReader(in);
        if (!ExtM3uTag.asHeadIn(lineReader)) return null;
        M3uPlayListHeader header = new M3uPlayListHeader();
        Object lineTag = header.fill(lineReader);
        if (isVariantTag(lineTag)) return null;
        return forLines(rescourceURI, header, lineTag, lineReader, uriReMapper);
    }

    public static BitratePlaylist forLines(String rescourceURI, Reader in) throws IOException {
        return BitratePlaylist.forLines(rescourceURI, in, (UriReMapper)null);
    }

    /**
     * @param rescourceURI
     * @param lineTag,     the second non-empty line of playlist
     * @param lineReader,  the remaining lines of playlist
     * @return
     * @throws java.io.IOException
     */
    protected static BitratePlaylist forLines(String rescourceURI,
                                              M3uPlayListHeader header,
                                              Object lineTag,
                                              LineReader lineReader,
                                              UriReMapper uriReMapper) throws IOException {
        BitratePlaylist bitratePlaylist = new BitratePlaylist();
        bitratePlaylist.resourceURI = rescourceURI;
        bitratePlaylist.header = header;
        //bitratePlaylist.playListTags.add(bitratePlaylist.headTag);
        MakePlayListHelper makeHelper = new MakePlayListHelper();
        if (header != null && header.getMediaSequenceTag() != null && header.getMediaSequenceTag().getMediaSequence() > -1) {
            makeHelper.nextSequenceNumber = header.getMediaSequenceTag().getMediaSequence();
        }
        while (lineTag != null) {
            makePlayList(lineTag, bitratePlaylist, lineReader, makeHelper, header, uriReMapper);
            lineTag = parseLine(lineReader);
        }
        return bitratePlaylist;
    }

    private static class MakePlayListHelper {
        int nextSequenceNumber = -1;
        ExtKeyTag keyTag;

        int nextSequenceNumber() {
            return ++nextSequenceNumber;
        }

        ExtKeyTag getKeyTag(ExtKeyTag keyTag) {
            if (keyTag == null) {
                return this.keyTag != null ? (ExtKeyTag) this.keyTag.clone() : null;
            }
            if (this.keyTag == null) {
                this.keyTag = keyTag;
                return keyTag;
            }
            if (!this.keyTag.equals(keyTag)) {
                this.keyTag = keyTag;
                return keyTag;
            }
            return keyTag;
        }
    }

    private static void makePlayList(Object lineTag,
                                     BitratePlaylist bitratePlaylist,
                                     LineReader lineReader,
                                     MakePlayListHelper makeHelper,
                                     M3uPlayListHeader header,
                                     UriReMapper uriReMapper) throws IOException {
        if (lineTag instanceof ExtEndListTag) {
            bitratePlaylist.endListTag = (ExtEndListTag) lineTag;
        }

        if (lineTag instanceof M3uTag) {
            RecordMarker recordMarker = new RecordMarker();
            boolean filled = recordMarker.fillFrom((M3uTag) lineTag, lineReader, uriReMapper);
            if (filled) {
                if (recordMarker.getAdSignalTag() != null && !recordMarker.getAdSignalTag().isBlackout())
                    bitratePlaylist.expandable = true;
                if (header != null && header.getMediaSequenceTag() != null && header.getMediaSequenceTag().getMediaSequence() > -1) {
                    recordMarker.setSequenceNumber(makeHelper.nextSequenceNumber());
                }
                recordMarker.setKeyTag(makeHelper.getKeyTag(recordMarker.getKeyTag()));
                //bitratePlaylist.playListTags.add(recordMarker);
                bitratePlaylist.recordMarkers.add(recordMarker);

                if (recordMarker.getReplaceSignalTag() != null || (recordMarker.getAdSignalTag() != null && recordMarker.getAdSignalTag().isBlackout())) {
                    bitratePlaylist.hasAlternateContent = true;
                }
            } else {
                //bitratePlaylist.playListTags.add((M3uTag) lineTag);
            }
        }
    }

    public ExtEndListTag getEndListTag() {
        return endListTag;
    }

    public ExtEndListTag getOrNewEndListTag() {
        if (this.endListTag == null) {
            this.endListTag = new ExtEndListTag();
        }
        return endListTag;
    }

    public void setEndListTag(ExtEndListTag endListTag) {
        this.endListTag = endListTag;
    }

    public List<RecordMarker> getRecordMarkers() {
        return recordMarkers;
    }

    public void setRecordMarkers(List<RecordMarker> recordMarkers) {
        this.recordMarkers = recordMarkers;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public boolean isHasAlternateContent() {
        return hasAlternateContent;
    }

    public void setHasAlternateContent(boolean hasAlternateContent) {
        this.hasAlternateContent = hasAlternateContent;
    }

    @Override
    public String emit() {
        StringBuilder stringBuilder = new StringBuilder();
        this.header.emit(stringBuilder);

        ExtKeyTag key = null;
        for (RecordMarker recordMarker : this.recordMarkers) {
            if (key == null) {
                key = recordMarker.getKeyTag();
            } else {
                if (key.equals(recordMarker.getKeyTag())) {
                    recordMarker.setKeyTag(null);
                } else {
                    if (recordMarker.getKeyTag() != null) {
                        key = (ExtKeyTag) recordMarker.getKeyTag().clone();
                        if (recordMarker.isSegmentURIAddressedAdContent()) {
                            String locationBase = StringUtils.substringBeforeLast(recordMarker.getKeyTag().getUri(), M3uTag.C_SLASH);
                            //Why use this constant key uri????
                            recordMarker.getKeyTag().setUri(locationBase + "/ad/a/278.key");
                        }
                    }
                }
            }
            recordMarker.emit(stringBuilder);
        }

        if (this.endListTag != null) this.endListTag.emit(stringBuilder);
        return stringBuilder.toString();
    }

    @Override
    public BitratePlaylist clone() {
        BitratePlaylist bitratePlaylist = (BitratePlaylist) super.clone();
        bitratePlaylist.header = this.header.clone();
        if (this.endListTag != null) bitratePlaylist.endListTag = (ExtEndListTag) this.endListTag.clone();
        bitratePlaylist.recordMarkers = new ArrayList<>();
        for (RecordMarker recordMarker : this.recordMarkers) {
            bitratePlaylist.recordMarkers.add(recordMarker.clone());
        }
        return bitratePlaylist;
    }
}
