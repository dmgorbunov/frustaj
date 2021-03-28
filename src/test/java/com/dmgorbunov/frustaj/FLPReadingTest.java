package com.dmgorbunov.frustaj;

import com.dmgorbunov.frustaj.data.FLPReader;
import com.dmgorbunov.frustaj.model.ProjectFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FLPReadingTest {

    private static final Logger log = LoggerFactory.getLogger(FLPReadingTest.class);
    private final FLPReader reader = new FLPReader();

    @ParameterizedTest
    @MethodSource("readingTestDataProvider")
    @DisplayName("FLP Reading")
    void flpReadingTest(String filename, String author, String genre, String title, String version, int minutesSpent) {
        File flp = new File(ClassLoader.getSystemResource(filename).getFile());

        ProjectFile projectFile = reader.readFile(flp.toPath());

        log.info("{}", projectFile);
        Assertions.assertEquals(projectFile.getAuthor(), author);
        Assertions.assertEquals(projectFile.getGenre(), genre);
        Assertions.assertEquals(projectFile.getTitle(), title);
        Assertions.assertEquals(projectFile.getFlVersion(), version);
        Assertions.assertEquals(projectFile.getTimeSpent().toMinutes(), minutesSpent);
    }

    private static Stream<Arguments> readingTestDataProvider() {
        return Stream.of(
                Arguments.arguments("flp-files/Test001.flp", "Test author", "Test genre", "Test title", "20.8.0.1377", 35),
                Arguments.arguments("flp-files/Test002.flp", "Test author #2", "Industrial", "Test project #2", "20.8.3.1566", 1),
                Arguments.arguments("flp-files/Test003.flp", "Test author #3", "Folklore", "Test #3", "20.8.3.1566", 1)
        );
    }

    @Test
    @DisplayName("FLP Reading - playlist")
    void playlistReadingTest() {
        File flp = new File(ClassLoader.getSystemResource("flp-files/Test003_2.flp").getFile());
        ProjectFile projectFile = reader.readFile(flp.toPath());
        log.info("Content size: {}", projectFile.getContentSize());
        log.info("Plugins: {}", projectFile.getPlugins());
        log.info("Patterns: {}", projectFile.getPatterns());
    }

}
