package org.mds.video.hls.model.tags;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Randall.mo
 */
public abstract class ExtSignalTag extends M3uTag {
    public enum HandledAction {
        NONE, REPLACE, MERGE;
    }

    protected String signalId;

    protected String duration;
    protected HandledAction handledAction = HandledAction.NONE;

    public String getSignalId() {
        return signalId;
    }

    public ExtSignalTag setSignalId(String signalId) {
        this.signalId = signalId;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public ExtSignalTag setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public HandledAction getHandledAction() {
        return handledAction;
    }

    public ExtSignalTag setHandledAction(HandledAction handledAction) {
        this.handledAction = handledAction;
        return this;
    }

    public abstract boolean isBlackout();

    public ExtSignalTag setBlackout(boolean blackout) {
        return this;
    }
}
