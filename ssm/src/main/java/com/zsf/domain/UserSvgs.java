package com.zsf.domain;

import java.util.Objects;

public class UserSvgs {
    private Integer id;

    private Integer userId;

    private Integer svgId;

    private Integer projectId;

    private String svgName;

    private String projectName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSvgId() {
        return svgId;
    }

    public void setSvgId(Integer svgId) {
        this.svgId = svgId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getSvgName() {
        return svgName;
    }

    public void setSvgName(String svgName) {
        this.svgName = svgName == null ? null : svgName.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSvgs userSvgs = (UserSvgs) o;
        return Objects.equals(svgId, userSvgs.svgId) &&
                Objects.equals(projectId, userSvgs.projectId) &&
                Objects.equals(svgName, userSvgs.svgName) &&
                Objects.equals(projectName, userSvgs.projectName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(svgId, projectId, svgName, projectName);
    }
}