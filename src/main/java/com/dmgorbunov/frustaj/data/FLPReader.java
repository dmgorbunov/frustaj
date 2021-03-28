package com.dmgorbunov.frustaj.data;

import com.dmgorbunov.frustaj.data.files.FLPFileUtils;
import com.dmgorbunov.frustaj.model.Project;
import com.dmgorbunov.frustaj.model.ProjectCollection;
import com.dmgorbunov.frustaj.model.ProjectFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class FLPReader {

    private final Logger log = LoggerFactory.getLogger(FLPReader.class);
    private final List<String> excludedKeywords;

    public FLPReader() {
        this.excludedKeywords = new ArrayList<>();
    }

    public FLPReader(Configuration configuration) {
        this.excludedKeywords = configuration.excludedKeywords;
    }

    private void logEvent(FLPEventType event, Object value) {
        if (!event.isSkippable())
            log.info("({}) {}={}", event.getValue(), event, value);
    }

    private LocalDateTime baseTime() {
        return LocalDateTime.of(1899, Month.DECEMBER, 29, 23, 59, 59);
    }

    public ProjectCollection readDirectory(String directory) {
        List<Project> projectList = new ArrayList<>();

        try {
            List<Path> paths = Files.find(Paths.get(directory), Integer.MAX_VALUE, FLPFileUtils.FILE_MATCHER).collect(Collectors.toList());

            for (Path path : paths) {
                String projectName = FLPFileUtils.processName(path.getFileName().toString(), excludedKeywords);
                Project project = projectList.stream().filter(p -> p.getName().equals(projectName))
                        .findFirst().orElse(new Project(projectName));
                ProjectFile projectFile = readFile(path);
                project.addFile(projectFile);
                projectList.add(project);
            }
        } catch (IOException e) {
            log.error("Failed to get files for dir: {}", directory);
        }

        Collections.sort(projectList);
        return new ProjectCollection(projectList);
    }

    public ProjectFile readFile(Path path) {

        ProjectFile projectFile = new ProjectFile();
        projectFile.setPath(path);
        String filename = path.toAbsolutePath().toString();


        try (DataInputStream in = new DataInputStream(new FileInputStream(filename))) {

            String fileHeader = decode(in.readNBytes(4), StandardCharsets.UTF_8);
            if (!fileHeader.equals("FLhd")) {
                log.error("Not a valid FL project file: {}", filename);
                projectFile.setCorrupted(true);
                return projectFile;
            } else {
                log.debug("File header: {}", fileHeader);
            }

            log.debug("Length: {}", in.readNBytes(4));
            log.debug("Format: {}", in.readNBytes(2));
            log.debug("nChannels: {}", in.readNBytes(2));
            log.debug("BeatDiv: {}", in.readNBytes(2));

            String dataHeader = decode(in.readNBytes(4), StandardCharsets.UTF_8);
            if (!dataHeader.equals("FLdt")) {
                projectFile.setCorrupted(true);
                return projectFile;
            } else {
                log.debug("Data chunk start");
            }

            byte[] dataLength = in.readNBytes(4);
            log.debug("Chunk length: {}", ByteBuffer.wrap(dataLength).getInt());

            int currentChannelType = 0;
            String currentPlugin = "";

            Map<String,List<String>> generators = new HashMap<>();

            while (in.available() > 0) {

                FLPEventType nextEvent = readNextEvent(in);

                switch (nextEvent.getSubType()) {
                    case BYTE -> {
                        int value = in.read();
                        logEvent(nextEvent, value);
                        if (nextEvent.equals(FLPEventType.CHANNEL_TYPE)) {
                            currentChannelType = value;
                        }
                    }
                    case WORD -> {
                        int value = in.readShort();
                        logEvent(nextEvent, value);
                        if (nextEvent.equals(FLPEventType.NEWCHAN)) {
//                            int channelType = in.read();
//
//                            if (TEXT_PLUGIN_NAME_DEFAULT.equals(readNextEvent(in))) {
//                                String defaultName = decode(readBlock(in));
//                                if (TEXT_PLUGIN.equals(readNextEvent(in))) {
//                                    byte[] unused = readBlock(in);
//                                    if (TEXT_PLUGIN_NAME.equals(readNextEvent(in))) {
//                                        String name = decode(readBlock(in));
//                                        generators.computeIfAbsent(defaultName, v -> new ArrayList<>()).add(name);
//                                    }
//                                }
//                            }
                        }
                    }
                    case INT -> {
                        int value = in.readInt();
                        logEvent(nextEvent, value);
                    }
                    case DATA -> {
                        byte[] bytes = readBlock(in);
                        logEvent(nextEvent, decode(bytes));

                        switch (nextEvent) {
                            case USED_FL_VERSION -> projectFile.setFlVersion(new String(Arrays.copyOfRange(bytes, 0, bytes.length-1)));
                            case SONG_TITLE -> projectFile.setTitle(decode(bytes));
                            case TEXT_AUTHOR -> projectFile.setAuthor(decode(bytes));
                            case TEXT_GENRE -> projectFile.setGenre(decode(bytes));
                            case TEXT_PROJECT_TIME -> {
                                projectFile.setCreatedAt(baseTime().plus(decodeDuration(bytes, 0, 8)));
                                projectFile.setTimeSpent(decodeDuration(bytes, 8, 8));
                            }
                            case TEXT_PLAYLISTITEMS -> {
                                projectFile.setContentSize(bytes.length / 32);
                                decodePatterns(bytes);
                            }
                            case TEXT_PLUGIN_NAME_DEFAULT -> {
                                currentPlugin = decode(bytes);
                            }
                            case TEXT_PLUGIN_NAME -> {
                                projectFile.addPlugin(currentChannelType == 0 ? "Sampler" : currentPlugin, decode(bytes));
                            }
                            case PATTERN_NAME -> {
                                String pattern = decode(bytes);
                                projectFile.addPlaylistPattern(pattern);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to parse file: {}", filename);
            e.printStackTrace();
            projectFile.setCorrupted(true);
        }

        return projectFile;
    }

    private FLPEventType readNextEvent(DataInputStream in) throws IOException {
        long nextEventId = Integer.toUnsignedLong(in.read());
        return FLPEventType.find(nextEventId);
    }

    private byte[] readBlock(DataInputStream in) throws IOException {
        long blockSize = 0;
        long shift = 0;
        long infoByte;

        do {
            infoByte = in.read();
            blockSize = blockSize | ((infoByte & 0x7F) << shift);
            shift += 7;
        } while ((infoByte & 0x80) != 0);

        return in.readNBytes((int)blockSize);
    }

    private String decode(byte[] a, Charset charset) {
        return new String(a, charset);
    }

    private String decode(byte[] a) {
        // NUL termination is a wrong salute, no one needs them
        return decode(a, StandardCharsets.UTF_16LE).replace("\0", "");
    }

    private Duration decodeDuration(byte[] a, int startIndex, int length) {

        double time = ByteBuffer.wrap(a, startIndex, length).order(ByteOrder.LITTLE_ENDIAN).getDouble();

        double hours = (time - (long) time) * 24;
        double minutes = (hours - (long) hours) * 60;
        double seconds = (minutes - (long) minutes) * 60;

        return Duration.of((long) time, ChronoUnit.DAYS)
                .plus((long) hours, ChronoUnit.HOURS)
                .plus((long) minutes, ChronoUnit.MINUTES)
                .plus((long) seconds, ChronoUnit.SECONDS);

    }

    private void decodePatterns(byte[] a) {
        int v = 0;
        int l = 0;
        System.out.printf("%1$4d:", l);
        for (byte b : a) {
            System.out.printf("%1$5d", b);
            v++;
            if (v > 31) {
                System.out.println();
                v = 0;
                l++;
                System.out.printf("%1$4d:", l);
            }
        }
    }

    public static class Configuration {
        private List<String> excludedKeywords = new ArrayList<>();

        public Configuration setExcludedKeywords(List<String> excludedKeywords) {
            this.excludedKeywords = excludedKeywords;
            return this;
        }

        public Configuration addExcludedKeyword(String keyword) {
            this.excludedKeywords.add(keyword);
            return this;
        }

        public Configuration addExcludedKeywords(List<String> keywords) {
            this.excludedKeywords.addAll(keywords);
            return this;
        }
    }
}

