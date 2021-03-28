package com.dmgorbunov.frustaj.model;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectCollection {

    private final SortedSet<Project> projects;

    public ProjectCollection(List<Project> projects) {
        this.projects = Collections.synchronizedSortedSet(new TreeSet<>(projects));
    }

    public void merge(ProjectCollection projectCollection) {
        this.projects.addAll(projectCollection.getProjects());
    }

    public int getSize() {
        return projects.size();
    }

    public synchronized List<Project> getProjects(Comparator<Project> projectComparator) {
        return projects.stream().sorted(projectComparator).collect(Collectors.toList());
    }

    public List<Project> getProjects() {
        return getProjects(Comparator.reverseOrder());
    }

    public synchronized List<Project> getProjectsByFileNumber() {
        return getProjects(Comparator.comparingInt(Project::getFileNumber).reversed());
    }
}
