package org.mds.video.hls.model.definitions;

import com.google.common.reflect.ClassPath;
import org.mds.video.hls.model.tags.M3uTag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author Randall.mo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "definitions")
@XmlType(name = "")
public class TagDefinitions {
    protected final static Logger log = LoggerFactory.getLogger(TagDefinitions.class);

    private final static String TAG_CLASS_PREFIX = "Tag";
    private final static String TAG_FIELD_PREFIX = "_TAG";
    private final static String TAG_ATTR_PREFIX = "ATTR_";
    private final static String TAG_OPTION_PREFIX = "OPTION_";
    private final static String ATTR_OPTION_MID = "_OPTION_";

    @XmlElement(name = "tag", required = true)
    private List<TagElement> tagElements;

    public List<TagElement> getTagElements() {
        return this.tagElements;
    }

    public static class TagDelegate {
        private Class tagClass;
        private Method createMethod;
        private M3uTag.Builder tagBuilder;

        TagDelegate(Class tagClass) {
            try {
                this.tagClass = tagClass;
                this.createMethod = tagClass.getMethod("forLine", new Class[]{String.class});
                this.createMethod.setAccessible(true);
                this.tagBuilder = (M3uTag.Builder) tagClass.getMethod("builder").invoke(tagClass);
            } catch (Exception ex) {

            }
        }

        public Class getTagClass() {
            return this.tagClass;
        }

        public Object forLine(String line) {
            try {
                if (this.tagBuilder != null)
                    return tagBuilder.build(line);
//                if (this.createMethod != null)
//                    return createMethod.invoke(tagClass, line);
            } catch (Exception ex) {
                log.error("Failed to create {} tag: {}", this.tagClass.getName(), ex.getCause());
            }
            return null;
        }
    }

    //this is for checking the error in unit test.
    protected final static List<String> errors = new CopyOnWriteArrayList<>();

    private static void handleError(String errMsg) {
        if (errors.size() > 10) {
            errors.clear();
        }
        errors.add(errMsg);
        log.warn(errMsg);
    }

    private static void cleanError() {
        errors.clear();
    }

    public static int errorCount() {
        return errors.size();
    }

    private static class TagClassInfo {
        String tagValue;
        Class tagClass;

        public TagClassInfo(String tagValue, Class tagClass) {
            this.tagValue = tagValue;
            this.tagClass = tagClass;
        }
    }

    private static class TagClassesInfo {
        Map<String, TagClassInfo> tagValueMap = new HashMap();
        Map<String, TagDelegate> tagDelegateMap = new HashMap();
    }

    public static Map<String, TagDelegate> load(String definitionsFileName, String tagsPackage) {
        if (StringUtils.isEmpty(definitionsFileName) || StringUtils.isEmpty(tagsPackage)) {
            throw new IllegalArgumentException("definitionsFileName or tagsPackage name can not be empty.");
        }
        cleanError();
        InputStream defFile = null;
        TagClassesInfo tagClassesInfo = makeTagClasses(tagsPackage);
        cleanError();
        try {
            defFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(definitionsFileName);
            if (defFile == null) {
                defFile = new FileInputStream(new File(definitionsFileName));
            }
        } catch (Exception ex) {

        }

        if (defFile == null) {
            String errMsg = String.format("M3u parser will use the default value: Can not find HLS definitions file %s", definitionsFileName);
            handleError(errMsg);
            return tagClassesInfo.tagDelegateMap;
        }

        try {
            if (defFile != null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(TagDefinitions.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                TagDefinitions tagDefinitions = (TagDefinitions) jaxbUnmarshaller.unmarshal(defFile);
                setTagDefinitions(tagClassesInfo, tagDefinitions);
            } else {
                String errMsg = "M3u paser will use the default value: Null inputstream of TagDefinitions";
                handleError(errMsg);
            }
        } catch (Throwable ex) {
            String errMsg = "M3u paser will use the default value: Failed to set the tag definitions: " + ex;
            handleError(errMsg);
        }
        return tagClassesInfo.tagDelegateMap;
    }

    private static TagClassesInfo makeTagClasses(String packageName) {
        TagClassesInfo tagClassesInfo = new TagClassesInfo();
        try {
            ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            Set<ClassPath.ClassInfo> classInfoSet = classPath.getTopLevelClasses(packageName);
            for (ClassPath.ClassInfo classInfo : classInfoSet) {
                String className = classInfo.getSimpleName();
                if (className.endsWith(TAG_CLASS_PREFIX)) {
                    Class cls = classInfo.load();
                    String tagName = className.substring(0, className.length() - 3);
                    String fieldName = tagName.toUpperCase() + TAG_FIELD_PREFIX;
                    Field field = FieldUtils.getField(cls, fieldName, true);

                    if (field != null) {
                        String fieldValue = (String) FieldUtils.readStaticField(field, true);
                        tagClassesInfo.tagValueMap.put(tagName.toLowerCase(), new TagClassInfo(fieldValue, cls));
                        tagClassesInfo.tagDelegateMap.put(fieldValue, new TagDelegate(cls));
                    } else {
                        tagClassesInfo.tagValueMap.put(tagName.toLowerCase(), new TagClassInfo(null, cls));
                    }
                }
            }
        } catch (Exception ex) {
            String errMsg = "Failed to load all tag classes: " + ex;
            handleError(errMsg);
            log.error("", ex);
        }
        return tagClassesInfo;
    }

    private static void setTagDefinitions(TagClassesInfo tagClassesInfo, TagDefinitions tagDefinitions) {
        List<TagElement> tagElements = tagDefinitions.getTagElements();
        if (tagElements == null || tagElements.isEmpty()) {
            return;
        }

        for (TagElement tagElement : tagElements) {
            String name = tagElement.getName();
            String tagValue = tagElement.getValue();
            TagClassInfo tagClassInfo = tagClassesInfo.tagValueMap.get(name.toLowerCase());
            if (tagClassInfo == null) {
                String errMsg = String.format("Cannot find tag class with name %s", name);
                handleError(errMsg);
                continue;
            }

            TagDelegate tagDelegate = tagClassesInfo.tagDelegateMap.get(tagClassInfo.tagValue);

            Class tagClass = tagClassInfo.tagClass;
            setTagElement(tagClass, tagElement);
            if (tagDelegate != null) {
                tagClassesInfo.tagDelegateMap.remove(tagClassInfo.tagValue);
                tagClassesInfo.tagDelegateMap.put(tagValue, tagDelegate);
            }
            tagClassInfo.tagValue = tagValue;
        }
    }

    private static boolean setTagClassField(Class tagClass, String fieldName, String fieldValue) {
        if (fieldValue == null) {
            return true;
        }
        try {
            Field tagField = FieldUtils.getField(tagClass, fieldName, true);
            if (tagField == null) return true;
            FieldUtils.writeStaticField(tagField, fieldValue);
            return true;
        } catch (Throwable ex) {
            String errMsg = String.format("Failed to set tag field '%s' of class '%s' : %s", fieldName, tagClass.getName(), ex);
            handleError(errMsg);
            return false;
        }
    }

    private static void setTagElement(Class tagClass, TagElement tagElement) {
        String tagFieldName = tagElement.getName().toUpperCase();

        if (!setTagClassField(tagClass, tagFieldName + TAG_FIELD_PREFIX, tagElement.getValue())) {
            return;
        }

        setTagValueOptions(tagClass, tagElement);

        setTagAttributes(tagClass, tagElement);
    }

    private static void setTagValueOptions(Class tagClass, TagElement tagElement) {
        List<TagValueOption> tagValueOptions = tagElement.getValueOptions();
        List<TagValueOption> attrValueOptions = tagElement.getValueOptions();
        if (tagValueOptions != null && !tagValueOptions.isEmpty()) {
            for (TagValueOption tagValueOption : attrValueOptions) {
                String optionFieldName = TAG_OPTION_PREFIX + tagValueOption.getName().toUpperCase();
                if (!setTagClassField(tagClass, optionFieldName, tagValueOption.getValue())) {
                    continue;
                }
            }
        }
    }

    private static void setTagAttributes(Class tagClass, TagElement tagElement) {
        List<TagAttribute> tagAttributes = tagElement.getTagAttributes();
        if (tagAttributes == null || tagAttributes.isEmpty()) {
            return;
        }

        for (TagAttribute tagAttribute : tagAttributes) {
            String attrName = tagAttribute.getName().toUpperCase();
            if (!setTagClassField(tagClass, TAG_ATTR_PREFIX + attrName, tagAttribute.getValue())) {
                continue;
            }
            List<TagValueOption> attrValueOptions = tagAttribute.getValueOptions();
            if (attrValueOptions == null || attrValueOptions.isEmpty()) {
                continue;
            }
            for (TagValueOption tagValueOption : attrValueOptions) {
                String optionFieldName = attrName + ATTR_OPTION_MID + tagValueOption.getName().toUpperCase();
                if (!setTagClassField(tagClass, optionFieldName, tagValueOption.getValue())) {
                    continue;
                }
            }
        }
    }
}
