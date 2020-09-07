package com.dexlock.jobfairapp.models;

public class Eligibility {
    private String course;
    private String branch;
    private Float cutOff;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Float getCutOff() {
        return cutOff;
    }

    public void setCutOff(Float cutOff) {
        this.cutOff = cutOff;
    }
}
