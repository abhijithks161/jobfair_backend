package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.JobFair;
import com.dexlock.jobfairapp.models.JobSeekerPerformance;
import com.dexlock.jobfairapp.resources.helpers.PerformanceResourceHelper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class PerformanceDAO extends BaseDAO<JobSeekerPerformance> {

    PerformanceResourceHelper helper = new PerformanceResourceHelper();

    @Override
    public MongoCollection<JobSeekerPerformance> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.PERFORMANCE_COLL, JobSeekerPerformance.class);
    }

    @Override
    public void create(JobSeekerPerformance object) {
        getCollection().insertOne(object);
    }

    public void updateApplied(String jobFairId, String userId) {
        Document searchDoc = new Document();
        searchDoc.put("jobFairId", new ObjectId(jobFairId));
        searchDoc.put("jobseekerId", new ObjectId(userId));
        JobFair jobFair = helper.fetchJobFair(jobFairId);
        Document updateDoc = new Document("$set", new Document("jobFairDate", jobFair.getDate())
                .append("jobFairId", new ObjectId(jobFairId))
                .append("jobseekerId", new ObjectId(userId))).append("$inc", new Document("applied", 1));
        UpdateOptions options = new UpdateOptions().upsert(true);
        getCollection().updateOne(searchDoc, updateDoc, options);
    }

    public void updateAppeared(String jobFairId, String userId) {
        Document searchDoc = new Document();
        searchDoc.put("jobFairId", new ObjectId(jobFairId));
        searchDoc.put("jobseekerId", new ObjectId(userId));
        Document updateDoc = new Document("$inc", new Document("appeared", 1));
        getCollection().updateOne(searchDoc, updateDoc);
    }

    public void updateSelected(String jobFairId, String userId) {
        Document searchDoc = new Document();
        searchDoc.put("jobFairId", new ObjectId(jobFairId));
        searchDoc.put("jobseekerId", new ObjectId(userId));
        Document updateDoc = new Document("$inc", new Document("selected", 1));
        getCollection().updateOne(searchDoc, updateDoc);
    }

    public ArrayList<JobSeekerPerformance> viewPerformance(String id) {
        Document searchDoc = new Document("jobseekerId", new ObjectId(id));
        ArrayList<JobSeekerPerformance> performances = new ArrayList<>();
        FindIterable<JobSeekerPerformance> iterable = getCollection().find(searchDoc);
        iterable.into(performances);
        return performances;
    }
}
