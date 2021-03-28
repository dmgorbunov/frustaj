package com.dmgorbunov.frustaj;

import com.dmgorbunov.frustaj.data.FLPReader;
import com.dmgorbunov.frustaj.model.Project;
import com.dmgorbunov.frustaj.model.ProjectCollection;
import com.dmgorbunov.frustaj.tools.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FLPProcessingTest {

    private static final Logger log = LoggerFactory.getLogger(FLPProcessingTest.class);

    private final FLPReader reader = new FLPReader();

    @Test
    void durationSortingTest() {
        ProjectCollection collection = reader.readDirectory(ClassLoader.getSystemResource("flp-files/").getFile());

        Assertions.assertEquals(3, collection.getSize(), "Wrong set size");

        Project p = collection.getProjects().get(0);
        Assertions.assertEquals("Test001", p.getName(), "Wrong project name");
        Assertions.assertEquals("1 hours 10 minutes", StringUtils.formatDuration(p.getTotalTimeSpent()), "Wrong duration");
    }

    @Test
    void fileNumberSortingTest() {
        ProjectCollection collection = reader.readDirectory(ClassLoader.getSystemResource("flp-files/").getFile());

        Assertions.assertEquals(3, collection.getSize(), "Wrong set size");

        Project p = collection.getProjectsByFileNumber().get(0);
        Assertions.assertEquals("Test002", p.getName(), "Wrong project name");
        Assertions.assertEquals(3, p.getFileNumber(), "Wrong file number");
    }

}
