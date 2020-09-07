package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.AddQualificationRequest;
import com.dexlock.jobfairapp.models.EditJobSeekerRequest;
import com.dexlock.jobfairapp.models.JobSeeker;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JobSeekerDAO extends BaseDAO<JobSeeker> {
    public MongoCollection<JobSeeker> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.JOBSEEKER_COLL, JobSeeker.class);

    }

    public void create(JobSeeker object) {
        getCollection().insertOne(object);
    }

    public boolean updateQualification(AddQualificationRequest request) {
        Document searchDoc = new Document("_id", new ObjectId(request.getId()));
        Document updateDoc = new Document("$addToSet", new Document("qualifications", request.getQualification()));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0)
            return true;

        else
            return false;
    }

    public List<JobSeeker> fetchAll() {
        List<JobSeeker> jobSeekers = new ArrayList<JobSeeker>();
        FindIterable<JobSeeker> findIterable = getCollection().find();
        findIterable.into(jobSeekers);
        return jobSeekers;
    }

    // save uploaded file to new location
    public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
    }

    public boolean uploadImage(String id, String uploadedFileLocation) {
        Document searchDoc = new Document("_id", new ObjectId(id));
        Document updateDoc = new Document("$set", new Document("profilePic", uploadedFileLocation));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0) {

            return true;
        } else
            return false;
    }

    public JobSeeker fetchJobSeekerDetail(String id) {
        Document searchDoc = new Document("_id", new ObjectId(id));
        JobSeeker jobSeeker = getCollection().find(searchDoc).first();
        return jobSeeker;
    }

    public boolean editJobSeeker(EditJobSeekerRequest request) {
        Document searchDoc = new Document("_id", new ObjectId(request.getId()));
        Document updateDoc = new Document("$set", new Document("address", request.getAddress())
                .append("dob", request.getDob()));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;
    }

}
