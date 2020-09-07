package com.dexlock.jobfairapp.models;

import com.dexlock.jobfairapp.utils.serde.ObjectIdJacksonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Opening {

    @JsonSerialize(using = ObjectIdJacksonSerializer.class)
    private ObjectId id;
    private ObjectId jobFairId;
    private String companyName;
    private String companyEmail;
    private String opening;
    private int salaryMin;
    private int salaryMax;
    private Address address;
    private int vacancy;
    private int experience;
    private Eligibility eligibility;
    private String jobDescription;
    private ArrayList<String> applied;
    private int appliedCount;
    private ArrayList<String> appeared;
    private int appearedCount;
    private ArrayList<String> selected;
    private int selectedCount;
    private Location location;
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonSerialize(using = ObjectIdJacksonSerializer.class)
    public ObjectId getJobFairId() {
        return jobFairId;
    }

    public void setJobFairId(ObjectId jobFairId) {
        this.jobFairId = jobFairId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public int getVacancy() {
        return vacancy;
    }

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
    }

    public Eligibility getEligibility() {
        return eligibility;
    }

    public void setEligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public int getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(int salaryMin) {
        this.salaryMin = salaryMin;
    }

    public int getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(int salaryMax) {
        this.salaryMax = salaryMax;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<String> getApplied() {
        return applied;
    }

    public void setApplied(ArrayList<String> applied) {
        this.applied = applied;
    }


    public ArrayList<String> getAppeared() {
        return appeared;
    }

    public void setAppeared(ArrayList<String> appeared) {
        this.appeared = appeared;
    }


    public ArrayList<String> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }

    @JsonIgnore
    public int getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(int appliedCount) {
        this.appliedCount = appliedCount;
    }

    @JsonIgnore
    public int getAppearedCount() {
        return appearedCount;
    }

    public void setAppearedCount(int appearedCount) {
        this.appearedCount = appearedCount;
    }

    @JsonIgnore
    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
