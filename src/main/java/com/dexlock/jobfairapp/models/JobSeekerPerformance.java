package com.dexlock.jobfairapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import java.util.Date;

public class JobSeekerPerformance {
    private ObjectId jobseekerId;
    private ObjectId jobFairId;
    private Date jobFairDate;
    private int applied;
    private int appeared;
    private int selected;

    @JsonIgnore
    public ObjectId getJobseekerId() {
        return jobseekerId;
    }

    public void setJobseekerId(ObjectId jobseekerId) {
        this.jobseekerId = jobseekerId;
    }

    @JsonIgnore
    public ObjectId getJobFairId() {
        return jobFairId;
    }

    public void setJobFairId(ObjectId jobFairId) {
        this.jobFairId = jobFairId;
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
