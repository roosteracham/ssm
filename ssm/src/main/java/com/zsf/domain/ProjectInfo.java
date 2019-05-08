package com.zsf.domain;

import java.util.Objects;

public class ProjectInfo implements Comparable{
    private Integer id;

    private String projectName;

    private Integer projectId;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectInfo that = (ProjectInfo) o;
        return Objects.equals(projectName, that.projectName) &&
                Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(projectName, projectId);
    }

    @Override
    public int compareTo(Object o) {
        if (this.hashCode() > o.hashCode())
            return 1;
        else if (this.hashCode() < o.hashCode())
            return -1;
        return 0;
    }
}