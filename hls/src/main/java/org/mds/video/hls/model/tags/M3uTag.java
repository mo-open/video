package org.mds.video.hls.model.tags;

import java.io.Serializable;
import java.util.*;

import org.mds.video.hls.model.definitions.TagDefinitions;
import org.mds.video.hls.utils.StringUtils;
import org.slf4j.*;

/**
 * @author Randall.mo
 */
public abstract class M3uTag implements Serializable, Cloneable {
    protected static final Logger log = LoggerFactory.getLogger(M3uTag.class);
    public final static String S_CRLF = "\r\n";
    public final static String S_EMPTY = "";
    public final static char C_COLON = ':';
    public final static char C_COMMA = ',';
    public final static char C_AT = '@';
    public final static char C_EQUAL = '=';
    public final static char C_DQUOTE = '"';
    public final static char C_POUND = '#';
    public final static char C_SLASH = '/';

    private static Map<String, TagDefinitions.TagDelegate> tagCreators;

    static {
        tagCreators = TagDefinitions.load("HLSDefinitions.xml", M3uTag.class.getPackage().getName());
    }

    public static interface Builder{
        public M3uTag build(String line);
    }

    public static int loadTagDefinitions(String definitionFile) {
        tagCreators = TagDefinitions.load(definitionFile, M3uTag.class.getPackage().getName());
        return TagDefinitions.errorCount();
    }

    public static M3uTag forLine(String line) {
        String tag = StringUtils.substringBefore(line, C_COLON);

        TagDefinitions.TagDelegate tagCreator = tagCreators.get(tag);
        if (tagCreator != null) {
            return (M3uTag) tagCreator.forLine(line);
        }
        return null;
    }

    protected static boolean isLineFor(String line, String tag) {
        return !StringUtils.isEmpty(line) && line.startsWith(tag);
    }

    protected static String[] getAttributes(String line, Map<String, Integer> attrs) {
        String[] attributeArray = new String[attrs.size()];
        String attributes = StringUtils.substringAfter(line, C_COLON);

        //For performance, here does not use String.split()!
        int index = attributes.indexOf(C_EQUAL);
        if (index > 0) {
            String key = attributes.substring(0, index);
            int lastIndex = -1;
            while (true) {
                lastIndex = index;
                index = attributes.indexOf(C_EQUAL, index + 1);
                if (index == -1) {
                    attributeArray[attrs.get(key)] = attributes.substring(lastIndex + 1);
                    break;
                }
                String valueKey = attributes.substring(lastIndex + 1, index);
                int sepIndex = valueKey.lastIndexOf(C_COMMA);
                String value = valueKey.substring(0, sepIndex);
                attributeArray[attrs.get(key)] = value;
                key = valueKey.substring(sepIndex + 1);
            }
        }

        return attributeArray;
    }

    protected static interface AttributeHandler<T extends M3uTag> {
        public void findAttribute(T tag, String attributeName, String value);
    }

    protected static <T extends M3uTag> void scanAttributes(T tag, String line, AttributeHandler attributeHandler) {
        String attributes = StringUtils.substringAfter(line, C_COLON);

        //For performance, here does not use String.split()!
        int index = attributes.indexOf(C_EQUAL);
        if (index > 0) {
            String key = attributes.substring(0, index);
            int lastIndex = -1;
            while (true) {
                lastIndex = index;
                index = attributes.indexOf(C_EQUAL, index + 1);
                if (index == -1) {
                    attributeHandler.findAttribute(tag, key, attributes.substring(lastIndex + 1));
                    break;
                }
                String valueKey = attributes.substring(lastIndex + 1, index);
                int sepIndex = valueKey.lastIndexOf(C_COMMA);
                String value = valueKey.substring(0, sepIndex);
                attributeHandler.findAttribute(tag, key, value);
                key = valueKey.substring(sepIndex + 1);
            }
        }
    }

    protected static Map<String, String> getAttributeMap(String line) {
        Map<String, String> attributeMap = new HashMap<>(4);
        String attributes = StringUtils.substringAfter(line, C_COLON);

        //For performance, here does not use String.split()!
        int index = attributes.indexOf(C_EQUAL);
        if (index > 0) {
            String key = attributes.substring(0, index);
            int lastIndex = -1;
            while (true) {
                lastIndex = index;
                index = attributes.indexOf(C_EQUAL, index + 1);
                if (index == -1) {
                    attributeMap.put(key, attributes.substring(lastIndex + 1));
                    break;
                }
                String valueKey = attributes.substring(lastIndex + 1, index);
                int sepIndex = valueKey.lastIndexOf(C_COMMA);
                String value = valueKey.substring(0, sepIndex);
                attributeMap.put(key, value);
                key = valueKey.substring(sepIndex + 1);
            }
        }

        return attributeMap;
    }

    public M3uTag clone() {
        try {
            return (M3uTag) super.clone();
        } catch (Exception ex) {

        }
        return null;
    }

    public abstract void emit(StringBuilder inputOutput);
}
