package com.dexlock.jobfairapp.models;

public class JoinJobFairRequest {
    private String id;
    private ParticipatingCompany participatingCompany;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParticipatingCompany getParticipatingCompany() {
        return participatingCompany;
    }

    public void setParticipatingCompany(ParticipatingCompany participatingCompany) {
        this.participatingCompany = participatingCompany;
    }
}
