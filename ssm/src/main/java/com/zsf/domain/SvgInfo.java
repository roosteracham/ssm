package com.zsf.domain;

public class SvgInfo {
    private Integer id;

    private String name;

    private Integer svgId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
}