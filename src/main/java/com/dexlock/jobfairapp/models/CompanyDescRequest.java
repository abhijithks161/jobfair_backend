package com.dexlock.jobfairapp.models;

import java.util.List;

public class CompanyDescRequest {
    private List<String> emails;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
