package com.dexlock.jobfairapp.resources.helpers;

import com.dexlock.jobfairapp.dao.EmployerDAO;
import com.dexlock.jobfairapp.dao.JobSeekerDAO;
import com.dexlock.jobfairapp.models.Employer;
import com.dexlock.jobfairapp.models.JobSeeker;
import org.bson.Document;

public class UserResourceHelper {
    JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
    EmployerDAO employerDAO = new EmployerDAO();


    public String findUser(String email) {
        Document searchDoc = new Document("email", email);
        JobSeeker jobSeeker = jobSeekerDAO.getCollection().find(searchDoc).first();
        if (jobSeeker != null)
            return jobSeeker.getId().toHexString();

        else {
            Employer employer = employerDAO.getCollection().find(searchDoc).first();
            return employer.getId().toHexString();
        }
    }
}
