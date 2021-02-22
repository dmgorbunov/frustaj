package com.dmgorbunov.frustaj.data;

import com.dmgorbunov.frustaj.model.ProjectFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class FLPReader {

    private final Logger log = LoggerFactory.getLogger(FLPReader.class);

    private String readChunk(InputStream in, int length) throws IOException {
        return new String(in.readNBytes(length));
    }

    private void logEvent(FLPEventType event, Object value) {
        if (!event.isSkippable())
            log.info("({}) {}={}", event.getValue(), event, value);
    }

    private String byteToString(byte[] chunk) {
        StringBuilder result = new StringBuilder();
        for (byte b : chunk){
            result.append((char) b);
        }
        return result.toString();
    }

    private LocalDateTime baseTime() {
        return LocalDateTime.of(1899, Month.DECEMBER, 29, 23, 59, 59);
    }

    public ProjectFile read(String filename) {

        ProjectFile projectFile = new ProjectFile();
        projectFile.setPath(filename);

        try (DataInputStream in = new DataInputStream(new FileInputStream(filename))) {

            String fileHeader = readChunk(in, 4);
            if (!fileHeader.equals("FLhd")) {
                projectFile.setCorrupted(true);
                return projectFile;
            } else {
                log.info("File header: {}", fileHeader);
            }

            log.info("Length: {}", in.readNBytes(4));
            log.info("Format: {}", in.readNBytes(2));
            log.info("nChannels: {}", in.readNBytes(2));
            log.info("BeatDiv: {}", in.readNBytes(2));

            String dataHeader = readChunk(in, 4);
            if (!dataHeader.equals("FLdt")) {
                projectFile.setCorrupted(true);
                return projectFile;
            } else {
                log.info("Data chunk start");
            }

            byte[] dataLength = in.readNBytes(4);
            log.info("Chunk length: {}", ByteBuffer.wrap(dataLength).getInt());

            while (in.available() > 0) {

                long nextEvent = Integer.toUnsignedLong(in.read());

                if (nextEvent <= 63) {
                    logEvent(FLPEventType.find(nextEvent), in.read());
                } else if (nextEvent <= 127) {
                    logEvent(FLPEventType.find(nextEvent), in.readShort());
                } else if (nextEvent <= 191) {
                    logEvent(FLPEventType.find(nextEvent), in.readInt());
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

                    switch (FLPEventType.find(nextEvent)) {
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
            e.printStackTrace();
        }

        return projectFile;
    }

    private String decode(byte[] a) {
        // NUL termination is a wrong salute, no one needs them
        return new String(a, StandardCharsets.UTF_16LE).replace("\0", "");
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

