package org.mds.video.hls.model.definitions;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Randall.mo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "attr")
@XmlType(name = "")
public class TagAttribute extends TagBaseElement {
    @XmlElement(name = "option", required = true)
    private List<TagValueOption> valueOptions;

    public List<TagValueOption> getValueOptions() {
        return this.valueOptions;
    }
}
