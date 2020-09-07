package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.JobFair;
import com.dexlock.jobfairapp.models.JoinJobFairRequest;
import com.dexlock.jobfairapp.models.Opening;
import com.dexlock.jobfairapp.models.ParticipatingCompany;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class JobFairDAO extends BaseDAO<JobFair> {
    @Override
    public MongoCollection<JobFair> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.JOBFAIR_COLL, JobFair.class);
    }

    @Override
    public void create(JobFair object) {
        getCollection().insertOne(object);
    }

    public boolean joinJobFair(JoinJobFairRequest request) {
        Document searchDoc = new Document("_id", new ObjectId(request.getId()));
        Document updateDoc = new Document("$addToSet",
                new Document("participatingCompanies", request.getParticipatingCompany()));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;
    }

    public ArrayList<JobFair> fetchAll() {
        JobFairDAO jobFairDAO = new JobFairDAO();
        ArrayList<JobFair> jobFairs = new ArrayList<JobFair>();
        FindIterable<JobFair> fairFindIterable = getCollection().find();
        fairFindIterable.into(jobFairs);
        return jobFairs;
    }

    public ArrayList<JobFair> fetchAllJobFair(int page, int count) {
        JobFairDAO jobFairDAO = new JobFairDAO();
        ArrayList<JobFair> jobFairs = new ArrayList<JobFair>();
        FindIterable<JobFair> fairFindIterable = getCollection().find().skip(count * (page - 1)).limit(count)
                .sort(new Document("date", -1));
        fairFindIterable.into(jobFairs);
        return jobFairs;
    }


    public boolean editOpenings(Opening opening, String id) {
        ArrayList<Opening> openings = new ArrayList<>();

        Document searchDoc = new Document("_id", new ObjectId(id));
        JobFair jobFair = getCollection().find(searchDoc).first();
        if (jobFair != null) {
            ArrayList<ParticipatingCompany> companies = jobFair.getParticipatingCompanies();
            for (ParticipatingCompany company : companies) {
                if (company.getEmail().equalsIgnoreCase(opening.getCompanyEmail())) {
                    if (company.getOpenings() == null) {
                        openings.add(opening);
                        company.setOpenings(openings);
                    } else {
                        openings = company.getOpenings();
                        openings.add(opening);
                        company.setOpenings(openings);
                    }
                }
            }
        }
        Document updateDoc = new Document("$set", new Document("date", jobFair.getDate()).append("venue", jobFair.getVenue())
                .append("location", jobFair.getLocation())
                .append("participatingCompanies", jobFair.getParticipatingCompanies())
                .append("time", jobFair.getTime()));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;

    }

    public JobFair fetchJobFairDetail(String id) {
        Document searchDoc = new Document("_id", new ObjectId(id));
        return getCollection().find(searchDoc).first();
    }

    public ArrayList<Opening> viewOpenings(String jobFairId, String email) {
        Document searchDoc = new Document("_id", new ObjectId(jobFairId));
        JobFair jobFair = getCollection().find(searchDoc).first();
        ArrayList<ParticipatingCompany> companies = jobFair.getParticipatingCompanies();
        ArrayList<Opening> openings = new ArrayList<>();
        for (ParticipatingCompany company : companies) {
            if (company.getEmail().equalsIgnoreCase(email)) {
                if (company.getOpenings() != null)
                    openings = company.getOpenings();
            }
        }
        return openings;
    }

    public void updateAppliedTotal(String jobId) {
        Document searchDoc = new Document("_id", new ObjectId(jobId));
        Document updateDoc = new Document("$inc", new Document("appliedTotal", 1));
        getCollection().updateOne(searchDoc, updateDoc);
    }
}
