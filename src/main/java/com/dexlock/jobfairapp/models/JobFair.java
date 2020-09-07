package com.dexlock.jobfairapp.models;

import com.dexlock.jobfairapp.utils.serde.ObjectIdJacksonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;


public class JobFair {
    @SerializedName("_id")
    private ObjectId id;
    private Date date;
    private String venue;
    private String location;
    private String Time;
    private ArrayList<ParticipatingCompany> participatingCompanies;
    private int appliedTotal;

    public int getAppliedTotal() {
        return appliedTotal;
    }

    public void setAppliedTotal(int appliedTotal) {
        this.appliedTotal = appliedTotal;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonSerialize(using = ObjectIdJacksonSerializer.class)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public ArrayList<ParticipatingCompany> getParticipatingCompanies() {
        return participatingCompanies;
    }

    public void setParticipatingCompanies(ArrayList<ParticipatingCompany> participatingCompanies) {
        this.participatingCompanies = participatingCompanies;
    }
}
