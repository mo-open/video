package org.mds.video.hls.model;

import com.google.common.io.LineReader;
import org.mds.video.hls.model.tags.*;
import org.mds.video.hls.model.tags.composite.M3uPlayListHeader;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class M3uPlayList implements Serializable, Cloneable {
    protected String resourceURI;
    protected boolean expandable;
    protected M3uPlayListHeader header = new M3uPlayListHeader();
    protected List<M3uTag> remainedLines = new ArrayList<M3uTag>();

    public static interface UriReMapper {
        public String rewrite(String url);
    }

    public static M3uPlayList forLines(String rescourceURI, Reader in,
                                       Map<Class, UriReMapper> uriReMappers) throws IOException {
        LineReader lineReader = new LineReader(in);
        if (!ExtM3uTag.asHeadIn(lineReader)) return null;
        UriReMapper uriReMapper = null;
        if (uriReMappers != null) uriReMapper = uriReMappers.get(ExtFaxCmTag.class);
        M3uPlayListHeader header = new M3uPlayListHeader();
        Object lineTag = header.fill(lineReader, uriReMapper);

        if (isVariantTag(lineTag)) {
            if (uriReMappers != null) uriReMapper = uriReMappers.get(ExtStreamInfTag.class);
            return VariantPlaylist.forLines(rescourceURI, header, lineTag, lineReader, uriReMapper);
        }

        if (uriReMappers != null) uriReMapper = uriReMappers.get(ExtStreamInfTag.class);
        return BitratePlaylist.forLines(rescourceURI, header, lineTag, lineReader, uriReMapper);
    }

    public static M3uPlayList forLines(String rescourceURI, Reader in) throws IOException {
        return forLines(rescourceURI, in, null);
    }

    public M3uPlayListHeader getHeader() {
        return header;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public M3uPlayList setResourceURI(String variantURI) {
        this.resourceURI = variantURI;
        return this;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public static Object parseLine(LineReader lineReader) throws IOException {
        String line = lineReader.readLine();

        while (line != null) {
            if ("".equals(line.trim())) {
                line = lineReader.readLine();
                continue;
            }
            Object tagObj = M3uTag.forLine(line);
            if (tagObj != null) return tagObj;
            if (line.indexOf(M3uTag.C_POUND) != 0) return line;
            return UnknownTag.forLine(line);
        }
        return null;
    }

    protected static boolean isVariantTag(Object tagObject) {
        if ((tagObject instanceof ExtMediaTag) ||
                (tagObject instanceof ExtStreamInfTag))
            return true;
        return false;
    }

    public String emit() {
        StringBuilder stringBuilder = new StringBuilder();

        this.header.emit(stringBuilder);

        for (M3uTag tag : this.remainedLines) {
            if (tag != null) {
                tag.emit(stringBuilder);
            }
        }

        return stringBuilder.toString();
    }

    public M3uPlayList clone() {
        M3uPlayList m3uPlayList = null;
        try {
            m3uPlayList = (M3uPlayList) super.clone();
            m3uPlayList.header = this.header.clone();
            m3uPlayList.remainedLines = new ArrayList<>();
            for (M3uTag tag : this.remainedLines) {
                m3uPlayList.remainedLines.add(tag.clone());
            }
        } catch (Exception ex) {

        }
        return m3uPlayList;
    }
}
