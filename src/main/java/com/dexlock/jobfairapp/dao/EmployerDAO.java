package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.configurations.JobFairAppConfiguration;
import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.EditEmployerRequest;
import com.dexlock.jobfairapp.models.Employer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployerDAO extends BaseDAO<Employer> {
    public MongoCollection<Employer> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.EMPLOYER_COLL, Employer.class);
    }

    public void create(Employer object) {
        getCollection().insertOne(object);
    }

    public List<Employer> fetchAll() {
        List<Employer> employers = new ArrayList<Employer>();
        FindIterable<Employer> findIterable = getCollection().find();
        findIterable.into(employers);
        for (Employer employer : employers) {
            String ip = JobFairAppConfiguration.getHostIp();
            String uploadDir = JobFairAppConfiguration.getUploadDir();
            String filename = employer.getLogo();
            String url = "http://" + ip + ":8000" + uploadDir + filename;
            employer.setLogoUrl(url);
        }
        return employers;
    }

    public Employer fetchEmployerDetail(String id) {
        Document searchDoc = new Document("_id", new ObjectId(id));
        return getCollection().find(searchDoc).first();
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
        Document updateDoc = new Document("$set", new Document("logo", uploadedFileLocation));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0) {

            return true;
        } else
            return false;
    }

    public boolean editEmployer(EditEmployerRequest request) {
        Document searchDoc = new Document("_id", new ObjectId(request.getId()));
        Document updateDoc = new Document("$set", new Document("companyName", request.getCompanyName())
                .append("companyEmail", request.getCompanyEmail())
                .append("companyPhoneNumber", request.getCompanyPhoneNumber())
                .append("websiteUrl", request.getWebsiteUrl())
                .append("corporateIdentificationNumber", request.getCorporateIdentificationNumber())
                .append("yearOfEstablishment", request.getYearOfEstablishment())
                .append("noOfEmployees", request.getNoOfEmployees())
                .append("address", request.getAddress())
                .append("designation", request.getDesignation())
                .append("description", request.getDescription()));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;
    }

    public List<String> fetchDescription(List<String> emails) {
        List<String> descs = new ArrayList<>();
        for (String email : emails) {
            Employer employer = getCollection().find(new Document("companyEmail", email)).first();
            if (employer != null)
                descs.add(employer.getDescription());
            else
                descs.add("");
        }
        return descs;
    }
}
