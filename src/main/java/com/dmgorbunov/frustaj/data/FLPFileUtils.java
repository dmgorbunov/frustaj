package com.dmgorbunov.frustaj.data;

import com.dmgorbunov.frustaj.data.files.FLPNameModifier;
import com.dmgorbunov.frustaj.model.Project;
import com.dmgorbunov.frustaj.model.ProjectFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class FLPFileUtils {

    private static final String FLP_EXTENSION = ".flp";
    private static final Logger log = LoggerFactory.getLogger(FLPFileUtils.class);
    private static String KEYWORDS_EXCLUDE = System.getenv("flp.name.exclude");

    private static String processName(String input) {
        String v = input.replace(FLP_EXTENSION, "").trim();

        for (FLPNameModifier mod : FLPNameModifier.DEFAULT_MODIFIERS) {
            v = mod.getFunction().apply(v);
        }

        for (String excluded : KEYWORDS_EXCLUDE.split(",")) {
            v = v.replaceAll("([ -]+)?" + excluded + "([ -]+)?", "");
        }

        return FLPNameModifier.REMOVE_LEADING_AND_TRAILING_UNDERSCORES.getFunction().apply(v);
//        return v.replaceAll("^[_]+|[_]+$", "").trim();
    }

    private static final BiPredicate<Path, BasicFileAttributes> FILE_MATCHER = (path, attr) ->
            path.toString().endsWith(FLP_EXTENSION);


    public static Set<Project> process(String directory) {
        Set<Project> projectSet = new HashSet<>();

        try {
            FLPReader reader = new FLPReader();
            List<Path> paths = Files.find(Paths.get(directory), Integer.MAX_VALUE, FILE_MATCHER).collect(Collectors.toList());

            for (Path path : paths) {
                 String projectName = processName(path.getFileName().toString());
                 Project project = projectSet.stream().filter(p -> p.getName().equals(projectName))
                         .findFirst().orElse(new Project(projectName));
                 ProjectFile projectFile = reader.read(path);
                 project.addFile(projectFile);
                 projectSet.add(project);
            }
        } catch (IOException e) {
            log.error("Failed to get files for dir: {}", directory);
        }

        return projectSet;
    }
}
