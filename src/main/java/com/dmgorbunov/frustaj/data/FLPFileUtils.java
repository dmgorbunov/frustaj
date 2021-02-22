package com.dmgorbunov.frustaj.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FLPFileUtils {

    private static final Pattern FLP_PATTERN = Pattern.compile("([_ ])?([0-9]{1,5})?(.flp)$");
    private static final Logger log = LoggerFactory.getLogger(FLPFileUtils.class);

    private static String parseName(File file) {
        String name = file.getName();
        Matcher m = FLP_PATTERN.matcher(name);
        return m.find() ? name.substring(0, m.start()) : name;
    }

    public static List<Path> getFiles(String path) {
        try {
            return Files.walk(Paths.get(path))
                    .filter(p -> p.toString().endsWith(".flp"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static String processName(String input) {
        return input
                .replace(".flp", "")
                .replace("Parhelia - ", "")
                .replaceAll("^\\+|^[0-9]+([ -]+)?", "")
                .replaceAll("_[0-9]+", "")
                .replaceAll("\\([A-Za-z0-9 ]+\\)$", "")
                .trim();
    }

    public static void count(String path) {

        List<Path> files = getFiles(path);

        TreeMap<String, Set<Path>> projectMap = new TreeMap<>();

        List<String> excludedDirs = Arrays.asList("Unwanted", "Released");

        for (Path file : files) {
            if (excludedDirs.stream().noneMatch(d -> file.toAbsolutePath().toString().contains(d))) {
                String name = processName(file.getFileName().toString());
                projectMap.computeIfAbsent(name, k -> new HashSet<>()).add(file);
            }
        }

        log.info("Total aggregates: {}", projectMap.size());
        AtomicInteger i = new AtomicInteger(0);
        projectMap.entrySet().forEach(e -> {
            log.info("Project #{}: {}", i.incrementAndGet(), e.getKey());
        });

    }

}
