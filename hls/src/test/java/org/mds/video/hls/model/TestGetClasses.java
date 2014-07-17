package org.mds.video.hls.model;

import com.google.common.reflect.ClassPath;
import org.mds.video.hls.model.tags.M3uTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

/**
 * @author Randall.mo
 */
public class TestGetClasses {
    private static final Logger log = LoggerFactory.getLogger(TestGetClasses.class);

    @Test
    public void runTest() throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        Set<ClassPath.ClassInfo> classes = classPath.getTopLevelClasses(M3uTag.class.getPackage().getName());
//        for(ClassPath.ClassInfo classInfo:classes){
//            log.info(classInfo.getSimpleName());
//        }
        Object o = "";
        log.info("" + o.getClass());
    }
}
