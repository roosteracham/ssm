package com.zsf.domain;

import java.util.Objects;

public class RoleProjectSvgVO {
    private Integer id;

    private String roleId;

    private String projectId;

    private String svgId;

    private String projectName;

    private String svgName;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getSvgId() {
        return svgId;
    }

    public void setSvgId(String svgId) {
        this.svgId = svgId == null ? null : svgId.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getSvgName() {
        return svgName;
    }

    public void setSvgName(String svgName) {
        this.svgName = svgName == null ? null : svgName.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleProjectSvgVO that = (RoleProjectSvgVO) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(svgId, that.svgId) &&
                Objects.equals(projectName, that.projectName) &&
                Objects.equals(svgName, that.svgName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(projectId, svgId, projectName, svgName);
    }
}