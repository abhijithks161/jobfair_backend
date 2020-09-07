package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.EmployerPerformance;
import com.dexlock.jobfairapp.models.JobFair;
import com.dexlock.jobfairapp.resources.helpers.EmployerResourceHelper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class EmployerPerformanceDao extends BaseDAO<EmployerPerformance> {
    @Override
    public MongoCollection<EmployerPerformance> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.EMPLOYER_PERFORMANCE_COLL, EmployerPerformance.class);
    }

    @Override
    public void create(EmployerPerformance object) {
        getCollection().insertOne(object);
    }

    public void updateEmployerPerformance(String jobFairId, ObjectId employerId) {
        EmployerResourceHelper helper = new EmployerResourceHelper();
        Document searchDoc = new Document();
        searchDoc.put("jobFairId", new ObjectId(jobFairId));
        searchDoc.put("employerId", employerId);
        JobFair jobFair = helper.fetchJobFair(jobFairId);
        Document updateDoc = new Document("$set", new Document("jobFairDate", jobFair.getDate())
                .append("jobFairId", new ObjectId(jobFairId))
                .append("employerId", employerId))
                .append("$inc", new Document("applied", 1));
        UpdateOptions options = new UpdateOptions().upsert(true);
        getCollection().updateOne(searchDoc, updateDoc, options);

    }

    public void updateAppeared(String jobFairId, ObjectId employerId) {
        Document searchDoc = new Document();
        searchDoc.put("jobFairId", new ObjectId(jobFairId));
        searchDoc.put("employerId", employerId);
        Document updateDoc = new Document("$inc", new Document("appeared", 1));
        getCollection().updateOne(searchDoc, updateDoc);
    }

    public void updateSelected(String jobFairId, ObjectId employerId) {
        Document searchDoc = new Document();
        searchDoc.put("jobFairId", new ObjectId(jobFairId));
        searchDoc.put("employerId", employerId);
        Document updateDoc = new Document("$inc", new Document("selected", 1));
        getCollection().updateOne(searchDoc, updateDoc);
    }

    public ArrayList<EmployerPerformance> viewPerformance(String id) {
        Document searchDoc = new Document("employerId", new ObjectId(id));
        ArrayList<EmployerPerformance> performances = new ArrayList<>();
        FindIterable<EmployerPerformance> iterable = getCollection().find(searchDoc);
        iterable.into(performances);
        return performances;
    }
}
