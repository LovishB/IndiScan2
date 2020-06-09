package com.lavish.indiscan;

public class ModelAddProject {

    String project_id;
    String project_name;
    String project_date;

    public ModelAddProject(String project_id, String project_name, String project_date) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.project_date = project_date;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getProject_date() {
        return project_date;
    }
}
