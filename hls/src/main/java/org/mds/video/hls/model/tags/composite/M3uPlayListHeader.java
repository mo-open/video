package org.mds.video.hls.model.tags.composite;

import com.google.common.io.LineReader;
import org.mds.video.hls.model.M3uPlayList;
import org.mds.video.hls.model.tags.*;

import java.io.IOException;


/**
 * @author Randall.mo
 */
public class M3uPlayListHeader extends CompositeTag {
    private ExtM3uTag headTag = new ExtM3uTag();
    private ExtVersionTag versionTag;
    private ExtTargetDurationTag targetDurationTag;
    private ExtPlaylistTypeTag playlistTypeTag;
    private ExtMediaSequenceTag mediaSequenceTag;
    private ExtAllowCacheTag allowCacheTag;
    private ExtFaxCmTag faxCmTag;

    public Object fill(LineReader lineReader) throws IOException {
        return this.fill(lineReader, null);
    }

    public Object fill(LineReader lineReader, M3uPlayList.UriReMapper uriReMapper) throws IOException {
        Object lineTag = M3uPlayList.parseLine(lineReader);
        while (lineTag != null) {
            if (!fillHeader(lineTag, uriReMapper)) {
                return lineTag;
            }
            lineTag = M3uPlayList.parseLine(lineReader);
        }
        return null;
    }

    private boolean fillHeader(Object lineTag, M3uPlayList.UriReMapper uriReMapper) {
        if (lineTag instanceof ExtVersionTag) {
            this.versionTag = (ExtVersionTag) lineTag;
            return true;
        }

        if (lineTag instanceof ExtFaxCmTag) {
            this.faxCmTag = (ExtFaxCmTag) lineTag;
            if (uriReMapper != null) {
                this.faxCmTag.setUri(uriReMapper.rewrite(this.faxCmTag.getUri()));
            }
            return true;
        }

        if (lineTag instanceof ExtTargetDurationTag) {
            this.targetDurationTag = (ExtTargetDurationTag) lineTag;
            return true;
        }

        if (lineTag instanceof ExtPlaylistTypeTag) {
            this.playlistTypeTag = (ExtPlaylistTypeTag) lineTag;
            return true;
        }

        if (lineTag instanceof ExtMediaSequenceTag) {
            this.mediaSequenceTag = (ExtMediaSequenceTag) lineTag;
            return true;
        }

        if (lineTag instanceof ExtAllowCacheTag) {
            this.allowCacheTag = (ExtAllowCacheTag) lineTag;
            return true;
        }

        return false;
    }

    public ExtTargetDurationTag getTargetDurationTag() {
        return targetDurationTag;
    }

    public ExtTargetDurationTag getOrNewTargetDurationTag() {
        if (this.targetDurationTag == null) {
            this.targetDurationTag = new ExtTargetDurationTag();
        }
        return targetDurationTag;
    }

    public void setTargetDurationTag(ExtTargetDurationTag targetDurationTag) {
        this.targetDurationTag = targetDurationTag;
    }

    public ExtPlaylistTypeTag getPlaylistTypeTag() {
        return playlistTypeTag;
    }

    public ExtPlaylistTypeTag getOrNewPlaylistTypeTag() {
        if (this.playlistTypeTag == null) {
            this.playlistTypeTag = new ExtPlaylistTypeTag();
        }
        return playlistTypeTag;
    }

    public void setPlaylistTypeTag(ExtPlaylistTypeTag playlistTypeTag) {
        this.playlistTypeTag = playlistTypeTag;
    }

    public ExtMediaSequenceTag getMediaSequenceTag() {
        return mediaSequenceTag;
    }

    public ExtMediaSequenceTag getOrNewMediaSequenceTag() {
        if (this.mediaSequenceTag == null) {
            this.mediaSequenceTag = new ExtMediaSequenceTag();
        }
        return mediaSequenceTag;
    }

    public void setMediaSequenceTag(ExtMediaSequenceTag mediaSequenceTag) {
        this.mediaSequenceTag = mediaSequenceTag;
    }

    public ExtAllowCacheTag getAllowCacheTag() {
        return allowCacheTag;
    }

    public ExtAllowCacheTag getOrNewAllowCacheTag() {
        if (this.allowCacheTag == null) {
            this.allowCacheTag = new ExtAllowCacheTag();
        }
        return allowCacheTag;
    }

    public void setAllowCacheTag(ExtAllowCacheTag allowCacheTag) {
        this.allowCacheTag = allowCacheTag;
    }

    public ExtVersionTag getVersionTag() {
        return versionTag;
    }

    public ExtVersionTag getOrNewVersionTag() {
        if (this.versionTag == null) {
            this.versionTag = new ExtVersionTag();
        }
        return this.versionTag;
    }

    public void setVersionTag(ExtVersionTag versionTag) {
        this.versionTag = versionTag;
    }

    public ExtFaxCmTag getOrNewFaxCmTag() {
        if (this.faxCmTag == null) {
            this.faxCmTag = new ExtFaxCmTag();
        }
        return this.faxCmTag;
    }

    public ExtFaxCmTag getFaxCmTag() {
        return faxCmTag;
    }

    public void setFaxCmTag(ExtFaxCmTag faxCmTag) {
        this.faxCmTag = faxCmTag;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        this.headTag.emit(inputOutput);
        if (this.versionTag != null) this.versionTag.emit(inputOutput);
        if (this.faxCmTag != null) this.faxCmTag.emit(inputOutput);
        if (this.playlistTypeTag != null) this.playlistTypeTag.emit(inputOutput);
        if (this.mediaSequenceTag != null) this.mediaSequenceTag.emit(inputOutput);
        if (this.targetDurationTag != null) this.targetDurationTag.emit(inputOutput);
        if (this.allowCacheTag != null) this.allowCacheTag.emit(inputOutput);
    }

    @Override
    public M3uPlayListHeader clone() {
        M3uPlayListHeader header = (M3uPlayListHeader) super.clone();
        header.headTag = (ExtM3uTag) this.headTag.clone();
        if (this.versionTag != null) header.versionTag = (ExtVersionTag) this.versionTag.clone();
        if (this.faxCmTag != null) header.faxCmTag = (ExtFaxCmTag) this.faxCmTag.clone();
        if (this.playlistTypeTag != null)
            header.playlistTypeTag = (ExtPlaylistTypeTag) this.playlistTypeTag.clone();
        if (this.mediaSequenceTag != null)
            header.mediaSequenceTag = (ExtMediaSequenceTag) this.mediaSequenceTag.clone();
        if (this.targetDurationTag != null)
            header.targetDurationTag = (ExtTargetDurationTag) this.targetDurationTag.clone();
        if (this.allowCacheTag != null) header.allowCacheTag = (ExtAllowCacheTag) this.allowCacheTag.clone();

        return header;
    }
}
