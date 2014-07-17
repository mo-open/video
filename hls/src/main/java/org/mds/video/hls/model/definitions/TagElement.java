package org.mds.video.hls.model.definitions;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author Randall.mo
 */
public class TagElement extends TagBaseElement {
    @XmlElement(name = "attr", required = true)
    private List<TagAttribute> tagAttributes;
    @XmlElement(name = "option", required = true)
    private List<TagValueOption> valueOptions;

    public List<TagAttribute> getTagAttributes() {
        return this.tagAttributes;
    }

    public List<TagValueOption> getValueOptions() {
        return this.valueOptions;
    }
}
