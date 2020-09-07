package com.dexlock.jobfairapp.models;

import java.util.ArrayList;
import java.util.Date;

public class CreateJobFairRequest {
    private Date date;
    private String venue;
    private String time;
    private String location;
    private ArrayList<ParticipatingCompany> participatingCompany;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<ParticipatingCompany> getParticipatingCompany() {
        return participatingCompany;
    }

    public void setParticipatingCompany(ArrayList<ParticipatingCompany> participatingCompany) {
        this.participatingCompany = participatingCompany;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
