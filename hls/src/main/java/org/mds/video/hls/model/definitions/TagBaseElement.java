package org.mds.video.hls.model.definitions;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Randall.mo
 */
public class TagBaseElement {
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "extend")
    protected String extend;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getExtend() {
        return extend;
    }
}
