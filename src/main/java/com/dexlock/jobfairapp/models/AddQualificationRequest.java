package com.dexlock.jobfairapp.models;

public class AddQualificationRequest {
    private String id;
    private Qualification qualification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }
}
