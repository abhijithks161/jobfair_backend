package com.dexlock.jobfairapp.resources.helpers;

import com.dexlock.jobfairapp.dao.*;
import com.dexlock.jobfairapp.models.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;


public class JobSeekerResourceHelper {
    UserDAO userDAO = new UserDAO();
    JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
    OpeningDAO openingDAO = new OpeningDAO();
    JobFairDAO jobFairDAO = new JobFairDAO();
    PerformanceDAO performanceDAO = new PerformanceDAO();
    JobSeekerPerformance jobSeekerPerformance = new JobSeekerPerformance();

    public User fetchUser(String id) {
        User user = userDAO.fetchUser(id);
        return user;
    }

    public String registerJob(String id, String email) {
        Opening opening = openingDAO.getCollection().find(new Document("_id", new ObjectId(id))).first();
        if (opening.getVacancy() != 0) {
            if (opening.getApplied() == null) {
                if (openingDAO.registerJob(id, email)) {
                    return "applied";
                } else
                    return "failed";
            } else {
                if (opening.getApplied().contains(email)) {
                    return "already applied";
                } else {
                    if (openingDAO.registerJob(id, email))
                        return "applied";

                    else
                        return "failed";
                }
            }
        } else
            return "no vacancy";
    }

    public ArrayList<Opening> fetchMatchedOpenings(String userId, String jobFairId) {
        JobSeeker jobSeeker = jobSeekerDAO.fetchJobSeekerDetail(userId);
        ArrayList<Qualification> qualifications = jobSeeker.getQualifications();
        ArrayList<Opening> openings = openingDAO.fetchJobFairOpenings(jobFairId);
        ArrayList<Opening> matchedOpenings = new ArrayList<>();
        for (Qualification qualification : qualifications) {
            for (Opening opening : openings) {
                if (qualification.getCourse().equalsIgnoreCase(opening.getEligibility().getCourse())
                        && (qualification.getPercentage() >= opening.getEligibility().getCutOff())
                        && qualification.getBranch().equalsIgnoreCase(opening.getEligibility().getBranch())
                ) {
                    matchedOpenings.add(opening);
                }
            }
        }
        return matchedOpenings;
    }

    public void updateAppliedTotal(String jobId) {
        jobFairDAO.updateAppliedTotal(jobId);
    }

    public void updatePerformance(String jobFairId, String userId) {
        performanceDAO.updateApplied(jobFairId, userId);
    }


    public ArrayList<JobSeekerPerformance> viewPerformance(String id) {
        ArrayList<JobSeekerPerformance> performances = performanceDAO.viewPerformance(id);
        return performances;
    }

    public boolean verify(String email) {
        User user = userDAO.getCollection().find(new Document("email", email)).first();
        if (user.getVerified().equalsIgnoreCase("yes"))
            return true;
        else
            return false;
    }

    public ArrayList<Opening> fetchAllOpenings(String jobFairId) {
        ArrayList<Opening> openings = openingDAO.fetchAllOpenings(jobFairId);
        return openings;
    }

    public void updateEmployerPerformance(String jobFairId, String jobId) {
        Opening opening = openingDAO.fetchOpening(jobId);
        EmployerPerformanceDao employerPerformanceDao = new EmployerPerformanceDao();
        EmployerDAO employerDAO = new EmployerDAO();
        Employer employer = employerDAO.getCollection().find(new Document("companyEmail", opening.getCompanyEmail())).first();
        employerPerformanceDao.updateEmployerPerformance(jobFairId, employer.getId());
    }


}

