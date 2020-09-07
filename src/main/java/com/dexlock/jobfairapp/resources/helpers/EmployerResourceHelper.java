package com.dexlock.jobfairapp.resources.helpers;


import com.dexlock.jobfairapp.dao.*;
import com.dexlock.jobfairapp.models.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;


public class EmployerResourceHelper {
    UserDAO userDAO = new UserDAO();
    JobFairDAO jobFairDAO = new JobFairDAO();
    OpeningDAO openingDAO = new OpeningDAO();
    JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
    PerformanceDAO performanceDAO = new PerformanceDAO();
    JobSeekerResourceHelper helper = new JobSeekerResourceHelper();
    EmployerPerformanceDao employerPerformanceDao = new EmployerPerformanceDao();

    public User fetchEmployerDetail(String id) {
        User user = userDAO.fetchUser(id);
        return user;
    }


    public void createFair(JobFair jobFair) {
        jobFairDAO.create(jobFair);

    }

    public boolean joinFair(JoinJobFairRequest request) {
        if (jobFairDAO.joinJobFair(request))
            return true;
        else
            return false;
    }

    public boolean addOpenings(AddOpeningRequest request) {
        Opening opening = new Opening();
        opening.setCompanyName(request.getName());
        opening.setOpening(request.getOpening());
        opening.setSalaryMin(request.getSalaryMin());
        opening.setSalaryMax(request.getSalaryMax());
        opening.setCity(request.getCity());
        opening.setCompanyEmail(request.getEmail());
        opening.setVacancy(request.getVacancy());
        opening.setEligibility(request.getEligibility());
        opening.setJobDescription(request.getJobDescription());
        opening.setExperience(request.getExperience());
        opening.setJobFairId(new ObjectId(request.getJobFairId()));
        opening.setLocation(request.getLocation());
        openingDAO.create(opening);
        if (jobFairDAO.editOpenings(opening, request.getJobFairId()))
            return true;
        else
            return false;

    }

    public JobSeeker fetchJobSeekerDetail(String userId) {
        return jobSeekerDAO.fetchJobSeekerDetail(userId);
    }

    public boolean apearedForJob(String jobId, String email) {
        if (openingDAO.appearedForJob(jobId, email))
            return true;
        else
            return false;
    }

    public boolean selectedForJob(String jobId, String email) {
        if (openingDAO.selectedForJob(jobId, email))
            return true;
        else
            return false;
    }

//    public boolean performance(String email) {
//        EmployerPerformanceDao employerPerformanceDao = new EmployerPerformanceDao();
//        employerPerformanceDao.updateApplied();
//    }

    public void updatePerformanceAppeared(String jobFairId, String userId) {
        performanceDAO.updateAppeared(jobFairId, userId);
    }

    public void updatePerformanceSelected(String jobFairId, String userId) {
        performanceDAO.updateSelected(jobFairId, userId);
    }

    public String spotRegistration(String jobFairId, String jobId, String email) {
        return helper.registerJob(jobId, email);
    }

    public JobSeeker fetchJobSeekerDetailWithEmail(String email) {
        Document searchDoc = new Document("email", email);
        JobSeeker jobSeeker = jobSeekerDAO.getCollection().find(searchDoc).first();
        if (jobSeeker != null)
            return jobSeeker;
        else
            return null;
    }

    public void updateAppliedTotal(String jobId) {
        helper.updateAppliedTotal(jobId);
    }

    public void updatePerformance(String jobFairId, ObjectId id) {
        helper.updatePerformance(jobFairId, id.toHexString());
    }

    public boolean deleteOpening(String jobFairId, String jobId, String email) {
        Document searchDoc = new Document("_id", new ObjectId(jobId));
        DeleteResult deleteResult = openingDAO.getCollection().deleteOne(searchDoc);
        if (deleteResult.getDeletedCount() > 0) {
            JobFair jobFair = jobFairDAO.fetchJobFairDetail(jobFairId);
            ArrayList<ParticipatingCompany> companies = jobFair.getParticipatingCompanies();
            for (ParticipatingCompany company : companies) {
                ArrayList<Opening> openings = company.getOpenings();
                for (int i = 0; i < openings.size(); i++) {
                    if (openings.get(i).getId().toHexString().equalsIgnoreCase(jobId))
                        openings.remove(i);
                }
                company.setOpenings(openings);
            }
            Document searchJobFair = new Document("_id", new ObjectId(jobFairId));
            UpdateResult updateResult = jobFairDAO.getCollection().replaceOne(searchJobFair, jobFair);
            return true;
        } else
            return false;
    }

    public JobFair fetchJobFair(String jobFairId) {
        return jobFairDAO.getCollection().find(new Document("_id", new ObjectId(jobFairId))).first();
    }

    public void updateEmployerPerformanceAppeared(String jobFairId, String jobId) {
        Opening opening = openingDAO.fetchOpening(jobId);
        EmployerDAO employerDAO = new EmployerDAO();
        Employer employer = employerDAO.getCollection().find(new Document("companyEmail", opening.getCompanyEmail())).first();
        employerPerformanceDao.updateAppeared(jobFairId, employer.getId());
    }

    public void updateEmployerPerformanceSelected(String jobFairId, String jobId) {
        Opening opening = openingDAO.fetchOpening(jobId);
        EmployerDAO employerDAO = new EmployerDAO();
        Employer employer = employerDAO.getCollection().find(new Document("companyEmail", opening.getCompanyEmail())).first();
        employerPerformanceDao.updateSelected(jobFairId, employer.getId());
    }

    public ArrayList<EmployerPerformance> viewPerformance(String id) {
        ArrayList<EmployerPerformance> performances = employerPerformanceDao.viewPerformance(id);
        return performances;
    }

    public ArrayList<JobSeeker> viewAppliedCandidates(String jobId) {
        ArrayList<JobSeeker> jobSeekers = new ArrayList<>();
        Opening opening = openingDAO.fetchOpening(jobId);
        ArrayList<String> emails = opening.getApplied();
        FindIterable<JobSeeker> iterable = jobSeekerDAO.getCollection().find(new Document("email", new Document("$in", emails)));
        iterable.into(jobSeekers);
        return jobSeekers;
    }
}
