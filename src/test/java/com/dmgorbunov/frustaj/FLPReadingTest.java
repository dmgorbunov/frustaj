package com.dmgorbunov.frustaj;

import com.dmgorbunov.frustaj.data.FLPReader;
import com.dmgorbunov.frustaj.model.ProjectFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

    @ParameterizedTest
    @MethodSource("readingTestDataProvider")
    @DisplayName("FLP Reading")
    void testReadingFLP(String filename, String author, String genre, String title, String version, int minutesSpent) {
        File flp = new File(ClassLoader.getSystemResource(filename).getFile());
        FLPReader reader = new FLPReader();
        ProjectFile projectFile = reader.read(flp.toPath());

        log.info("{}", projectFile);
        Assertions.assertEquals(projectFile.getAuthor(), author);
        Assertions.assertEquals(projectFile.getGenre(), genre);
        Assertions.assertEquals(projectFile.getTitle(), title);
        Assertions.assertEquals(projectFile.getFlVersion(), version);
        Assertions.assertEquals(projectFile.getTimeSpent().toMinutes(), minutesSpent);
    }

    private static Stream<Arguments> readingTestDataProvider() {
        return Stream.of(
                Arguments.arguments("flp-files/Test001.flp", "Test author", "Test genre", "Test title", "20.8.0.1377", 35)
        );
    }

}
