package com.dmgorbunov.frustaj.data;

import com.dmgorbunov.frustaj.model.ProjectFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class FLPReader {

    private final Logger log = LoggerFactory.getLogger(FLPReader.class);

    private void logEvent(FLPEventType event, Object value) {
        if (!event.isSkippable())
            log.debug("({}) {}={}", event.getValue(), event, value);
    }

    private LocalDateTime baseTime() {
        return LocalDateTime.of(1899, Month.DECEMBER, 29, 23, 59, 59);
    }

    public ProjectFile read(Path path) {

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

            while (in.available() > 0) {

                long nextEventId = Integer.toUnsignedLong(in.read());
                FLPEventType nextEvent = FLPEventType.find(nextEventId);

                if (nextEventId <= 63) {
                    logEvent(nextEvent, in.read());
                } else if (nextEventId <= 127) {
                    logEvent(nextEvent, in.readShort());
                } else if (nextEventId <= 191) {
                    logEvent(nextEvent, in.readInt());
                } else {

                    long blockSize = 0;
                    long shift = 0;
                    long infoByte;

                    do {
                        infoByte = in.read();
                        blockSize = blockSize | ((infoByte & 0x7F) << shift);
                        shift += 7;
                    } while ((infoByte & 0x80) != 0);

                    byte[] bytes = in.readNBytes((int)blockSize);

                    switch (nextEvent) {
                        case USED_FL_VERSION -> projectFile.setFlVersion(new String(Arrays.copyOfRange(bytes, 0, bytes.length-1)));
                        case SONG_TITLE -> projectFile.setTitle(decode(bytes));
                        case TEXT_AUTHOR -> projectFile.setAuthor(decode(bytes));
                        case TEXT_GENRE -> projectFile.setGenre(decode(bytes));
                        case TEXT_PROJECT_TIME -> {
                            projectFile.setCreatedAt(baseTime().plus(decodeDuration(bytes, 0, 8)));
                            projectFile.setTimeSpent(decodeDuration(bytes, 8, 8));
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
}

