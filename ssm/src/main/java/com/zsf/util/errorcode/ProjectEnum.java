package com.zsf.util.errorcode;

public enum ProjectEnum {
    PROJECT_ENUM("projects", 0),
    SVG_ENUM("svgs", 1),
    ELEMENT_TYPE_ENUM("element-type", 2),
    ELEMENT_ENUM("elements", 3);

    private String project;

    private int index;

    public String getProject() {
        return project;
    }

    ProjectEnum(String project, int index) {
        this.project = project;
        this.index = index;
    }

    public static String getProject(int index) {
        for (ProjectEnum c : ProjectEnum.values()) {
            if (c.getIndex() == index) {
                return c.project;
            }
        }
        return null;
    }

    public void setProject(String error) {
        this.project = error;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
