package com.dmgorbunov.frustaj;

import com.dmgorbunov.frustaj.data.FLPFileUtils;
import com.dmgorbunov.frustaj.model.Project;
import com.dmgorbunov.frustaj.tools.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FLPProcessingTest {

    private static final Logger log = LoggerFactory.getLogger(FLPProcessingTest.class);

    @Test
    void directoryProcessingTest() {
        Set<Project> projectSet = FLPFileUtils.process(ClassLoader.getSystemResource("flp-files/").getFile());
        Assertions.assertEquals(1, projectSet.size(), "Wrong set size");

        Project p = projectSet.stream().findAny().orElse(new Project("Failed"));
        Assertions.assertEquals("Test001", p.getName(), "Wrong project name");
        Assertions.assertEquals("1 hours 10 minutes", StringUtils.formatDuration(p.getTotalTimeSpent()), "Wrong duration");
    }
}
