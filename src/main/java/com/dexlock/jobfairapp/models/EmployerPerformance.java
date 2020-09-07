package com.dexlock.jobfairapp.models;

import com.dexlock.jobfairapp.utils.serde.ObjectIdJacksonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.Date;

public class EmployerPerformance {
    private ObjectId jobFairId;
    private ObjectId employerId;
    private Date jobFairDate;
    private int applied;
    private int appeared;
    private int selected;

    @JsonSerialize(using = ObjectIdJacksonSerializer.class)
    public ObjectId getJobFairId() {
        return jobFairId;
    }

    public void setJobFairId(ObjectId jobFairId) {
        this.jobFairId = jobFairId;
    }

    @JsonSerialize(using = ObjectIdJacksonSerializer.class)
    public ObjectId getEmployerId() {
        return employerId;
    }

    public void setEmployerId(ObjectId employerId) {
        this.employerId = employerId;
    }

    public Date getJobFairDate() {
        return jobFairDate;
    }

    public void setJobFairDate(Date jobFairDate) {
        this.jobFairDate = jobFairDate;
    }

    public int getApplied() {
        return applied;
    }

    public void setApplied(int applied) {
        this.applied = applied;
    }

    public int getAppeared() {
        return appeared;
    }

    public void setAppeared(int appeared) {
        this.appeared = appeared;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
