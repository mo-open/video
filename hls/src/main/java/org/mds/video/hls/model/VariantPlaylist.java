package org.mds.video.hls.model;

import com.google.common.io.LineReader;
import org.mds.video.hls.model.tags.ExtM3uTag;
import org.mds.video.hls.model.tags.ExtMediaTag;
import org.mds.video.hls.model.tags.ExtStreamInfTag;
import org.mds.video.hls.model.tags.M3uTag;
import org.mds.video.hls.model.tags.composite.M3uPlayListHeader;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariantPlaylist extends M3uPlayList {
    private List<ExtStreamInfTag> variantStreams = new ArrayList<>();
    private List<ExtMediaTag> medias = new ArrayList<>();

    public static VariantPlaylist forLines(String resourceURI, Reader in, UriReMapper uriReMapper) throws IOException {
        LineReader lineReader = new LineReader(in);
        if (!ExtM3uTag.asHeadIn(lineReader)) return null;

        M3uPlayListHeader header = new M3uPlayListHeader();
        Object lineTag = header.fill(lineReader);

        if (!isVariantTag(lineTag)) return null;

        return forLines(resourceURI, header, lineTag, lineReader, uriReMapper);
    }

    public static VariantPlaylist forLines(String resourceURI, Reader in) throws IOException {
        return VariantPlaylist.forLines(resourceURI, in, (UriReMapper) null);
    }

    /**
     * @param resourceURI
     * @param lineTag,    the second non-empty line of play list
     * @param lineReader, the remaining lines of playlist
     * @return
     * @throws java.io.IOException
     */
    protected static VariantPlaylist forLines(String resourceURI,
                                              M3uPlayListHeader header,
                                              Object lineTag,
                                              LineReader lineReader,
                                              UriReMapper uriReMapper) throws IOException {
        VariantPlaylist variantPlaylist = new VariantPlaylist();
        variantPlaylist.resourceURI = resourceURI;

        variantPlaylist.header = header;

        while (lineTag != null) {
            variantPlaylist.remainedLines.add((M3uTag) lineTag);
            if (lineTag instanceof ExtStreamInfTag) {
                ExtStreamInfTag extStreamInfTag = (ExtStreamInfTag) lineTag;
                variantPlaylist.variantStreams.add(extStreamInfTag);
                lineTag = parseLine(lineReader);
                if (lineTag != null && lineTag instanceof String) {
                    if (uriReMapper != null) {
                        extStreamInfTag.setUri(uriReMapper.rewrite((String) lineTag));
                    } else
                        extStreamInfTag.setUri((String) lineTag);
                } else
                    continue;
            } else
                variantPlaylist.medias.add((ExtMediaTag) lineTag);
            lineTag = parseLine(lineReader);
        }

        return variantPlaylist;
    }

    public List<ExtStreamInfTag> getVariantStreams() {
        return this.variantStreams;
    }


    @Override
    public VariantPlaylist clone() {
        VariantPlaylist variantPlaylist = (VariantPlaylist) super.clone();
        variantPlaylist.variantStreams = new ArrayList<>();
        variantPlaylist.medias = new ArrayList<>();
        for (M3uTag tag : variantPlaylist.remainedLines) {
            if (tag instanceof ExtStreamInfTag) {
                variantPlaylist.variantStreams.add((ExtStreamInfTag) tag);
                continue;
            }
            if (tag instanceof ExtMediaTag) {
                variantPlaylist.medias.add((ExtMediaTag) tag);
                continue;
            }
        }
        return variantPlaylist;
    }
}
