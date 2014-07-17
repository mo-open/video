package org.mds.video.hls.model.definitions;

import org.mds.video.hls.model.tags.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TagDefinitionsTest {

    @Test
    public void testCommonLoad() {
        TagDefinitions.load("test-HLSDefinitions1.xml", M3uTag.class.getPackage().getName());
        Assert.assertTrue(TagDefinitions.errorCount() == 0);
        Assert.assertEquals(ExtSignalReplaceTag.EXTSIGNALREPLACE_TAG,"#EXT-X-BLACKOUT-SPAN-TEST");
        Assert.assertEquals(ExtSignalExitTag.SIGNALID_OPTION_BLACKOUT,"A-BLACKOUT");

        TagDefinitions.load("test-HLSDefinitions2.xml", M3uTag.class.getPackage().getName());
        Assert.assertTrue(TagDefinitions.errorCount() == 0);
        Assert.assertEquals(ExtSignalReplaceTag.ATTR_SIGNALID,"Signal-ID");
        Assert.assertEquals(ExtSignalExitTag.ATTR_SIGNALID,"signal-Id");
        Assert.assertEquals(ExtSignalSpanTag.ATTR_SIGNALID,"signal-Id");
        Assert.assertEquals(ExtSignalReturnTag.ATTR_SIGNALID,"signal-Id");
        Assert.assertEquals(ExtTrackingTag.EXTTRACKING_TAG,"#EXT-TRACKING");

        TagDefinitions.load("test-HLSDefinitions3.xml", M3uTag.class.getPackage().getName());
        Assert.assertTrue(TagDefinitions.errorCount() != 0);
    }

} 
