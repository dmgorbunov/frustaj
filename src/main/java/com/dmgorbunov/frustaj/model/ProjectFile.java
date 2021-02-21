package com.dmgorbunov.frustaj.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class ProjectFile {
    private boolean isCorrupted = false;
    private String path;
    private String flVersion;
    private String title;
    private String author;
    private String genre;

    private LocalDateTime createdAt;
    private Duration timeSpent;

    public ProjectFile() {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
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

    @Override
    public String toString() {
        return String.format("Project{title=%s, createdDate=%s, time spent: %d hours, %d minutes}",
                title, createdAt, timeSpent.toHoursPart(), timeSpent.toMinutes());
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Duration timeSpent) {
        this.timeSpent = timeSpent;
    }
}
