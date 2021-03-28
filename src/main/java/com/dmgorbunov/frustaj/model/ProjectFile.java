package com.dmgorbunov.frustaj.model;

import com.dmgorbunov.frustaj.tools.StringUtils;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectFile {
    private boolean isCorrupted = false;
    private Path path;
    private String flVersion;
    private String title;
    private String author;
    private String genre;

    private int contentSize;
    private List<String> patterns = new ArrayList<>();
    private final Map<String, List<String>> plugins = new HashMap<>();

    private LocalDateTime createdAt;
    private Duration timeSpent;

    public ProjectFile() {}

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlVersion() {
        return flVersion;
    }

    public void setFlVersion(String flVersion) {
        this.flVersion = flVersion;
    }

    public boolean isCorrupted() {
        return isCorrupted;
    }

    public void setCorrupted(boolean corrupted) {
        isCorrupted = corrupted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Duration timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }

    public void addPlugin(String type, String name) {
        plugins.computeIfAbsent(type, v -> new ArrayList<>()).add(name);
    }

    public Map<String,List<String>> getPlugins() {
        return plugins;
    }

    public void addPlaylistPattern(String name) {
        this.patterns.add(name);
    }

    public List<String> getPatterns() {
        return patterns;
    }

    @Override
    public String toString() {
        return String.format("Project{file=%s, title=%s, createdDate=%s, time spent: %s}",
                path.getFileName(), title, createdAt, StringUtils.formatDuration(timeSpent));
    }
}
