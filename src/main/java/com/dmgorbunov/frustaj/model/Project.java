package com.dmgorbunov.frustaj.model;

import com.dmgorbunov.frustaj.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Project implements Comparable<Project> {

    private String name;
    private Duration totalTimeSpent = Duration.of(0, ChronoUnit.SECONDS);
    private final Set<ProjectFile> fileSet = new HashSet<>();
    private final static Logger log = LoggerFactory.getLogger(Project.class);

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFile(ProjectFile file) {
        this.fileSet.add(file);
        this.totalTimeSpent = this.totalTimeSpent.plus(file.getTimeSpent());
    }

    public int getFileNumber() {
        return fileSet.size();
    }

    public Duration getTotalTimeSpent() {
        return totalTimeSpent;
    }

    @Override
    public String toString() {
        return String.format("Project: %s \nTotal time spent: %s\nFile set:\n\t%s",
                name,
                StringUtils.formatDuration(totalTimeSpent),
                fileSet.stream().map(ProjectFile::toString).collect(Collectors.joining("\n\t")));
    }

    @Override
    public int compareTo(Project o) {
        return this.totalTimeSpent.compareTo(o.totalTimeSpent);
    }
}
