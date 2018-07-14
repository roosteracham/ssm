package com.zsf.domain;

public class ProjectDto {

    private int id;

    private String projectName;

    private String name;

    private int type;

    private int projectIndex;

    private int svgIndex;

    public int getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    public int getSvgIndex() {
        return svgIndex;
    }

    public void setSvgIndex(int svgIndex) {
        this.svgIndex = svgIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
