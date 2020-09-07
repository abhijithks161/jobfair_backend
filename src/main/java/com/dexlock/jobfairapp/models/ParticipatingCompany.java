package com.dexlock.jobfairapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class ParticipatingCompany {
    private String name;
    private String email;
    private ArrayList<Opening> openings;

    @JsonIgnore
    public ArrayList<Opening> getOpenings() {
        return openings;
    }

    public void setOpenings(ArrayList<Opening> openings) {
        this.openings = openings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
