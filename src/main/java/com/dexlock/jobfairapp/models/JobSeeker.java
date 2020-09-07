package com.dexlock.jobfairapp.models;

import com.dexlock.jobfairapp.utils.serde.ObjectIdJacksonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;

import java.util.ArrayList;

public class JobSeeker {
    @SerializedName("_id")
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String dob;
    @Email
    private String email;
    private String phoneNumber;
    private Address address;
    private ArrayList<Qualification> qualifications;
    private String profilePic;
    private String profilePicURL;
    private JobSeekerPerformance performance;
    private String verified;

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public JobSeekerPerformance getPerformance() {
        return performance;
    }

    public void setPerformance(JobSeekerPerformance performance) {
        this.performance = performance;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @JsonSerialize(using = ObjectIdJacksonSerializer.class)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<Qualification> qualifications) {
        this.qualifications = qualifications;
    }
}
