package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.Opening;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class OpeningDAO extends BaseDAO<Opening> {
    @Override
    public MongoCollection<Opening> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.OPENING_COLL, Opening.class);
    }

    @Override
    public void create(Opening object) {
        getCollection().insertOne(object);
    }

    public boolean registerJob(String id, String email) {

        Document searchDoc = new Document("_id", new ObjectId(id));
        Document updateDoc = new Document("$addToSet", new Document("applied", email));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0) {
            Document count = new Document("$inc", new Document("appliedCount", 1));
            getCollection().updateOne(searchDoc, count);
            return true;
        } else
            return false;
    }

    public boolean appearedForJob(String jobId, String email) {
        Document searchDoc = new Document("_id", new ObjectId(jobId));
        Document updateDoc = new Document("$addToSet", new Document("appeared", email));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0) {
            Document count = new Document("$inc", new Document("appearedCount", 1));
            getCollection().updateOne(searchDoc, count);
            return true;
        } else
            return false;
    }

    public boolean selectedForJob(String jobId, String email) {
        Document searchDoc = new Document("_id", new ObjectId(jobId));
        Document updateDoc = new Document("$addToSet", new Document("selected", email));
        UpdateResult updateResult = getCollection().updateOne(searchDoc, updateDoc);
        if (updateResult.getModifiedCount() > 0) {
            getCollection().updateOne(searchDoc, new Document("$inc", new Document("vacancy", -1)
                    .append("selectedCount", 1)));
            return true;
        } else
            return false;
    }

    public ArrayList<Opening> fetchAll() {
        ArrayList<Opening> openings = new ArrayList<Opening>();
        FindIterable<Opening> findIterable = getCollection().find();
        findIterable.into(openings);
        return openings;
    }

//    public ArrayList<Opening> searchOpenings(double lat, double lon,double radius) {
//        Position position = new Position(lat,lon);
//        Point point = new Point(position);
//        ArrayList<Opening> openings = new ArrayList<>();
//        Document searchDoc = new Document("location",new Document("$nearSphere",point).append("$maxDistance",radius));
//        FindIterable<Opening> iterable = getCollection().find(searchDoc);
//        iterable.into(openings);
//        return openings;
//    }

    public ArrayList<Opening> fetchJobFairOpenings(String id) {
        Document searchDoc = new Document("jobFairId", new ObjectId(id));
        ArrayList<Opening> openings = new ArrayList<>();
        FindIterable<Opening> iterable = getCollection().find(searchDoc);
        iterable.into(openings);
        return openings;
    }

    public ArrayList<Opening> searchOpenings(Opening opening) {
        ArrayList<Opening> matchedOpenings = new ArrayList<>();
        Document searchDoc = new Document();
        if (opening.getLocation() != null && opening.getLocation().getLatitude() != null
                && opening.getLocation().getLongitude() != null) {
            Position position = new Position(opening.getLocation().getLatitude(), opening.getLocation().getLongitude());
            Point point = new Point(position);
            searchDoc.put("location", new Document("$nearSphere", point)
                    .append("$maxDistance", opening.getLocation().getRadius()));
        }
        if (opening.getExperience() != 0) {
            searchDoc.put("experience", new Document("$lte", opening.getExperience()));
        }
        if (opening.getSalaryMin() != 0)
            searchDoc.put("salaryMin", new Document("$gte", opening.getSalaryMin()));
        if (opening.getSalaryMax() != 0)
            searchDoc.put("salaryMax", new Document("$lte", opening.getSalaryMax()));
        FindIterable<Opening> iterable = getCollection().find(searchDoc);
        iterable.into(matchedOpenings);
        return matchedOpenings;
    }

    public ArrayList<Opening> fetchAllOpenings(String jobFairId) {
        Document searchDoc = new Document("jobFairId", new ObjectId(jobFairId));
        ArrayList<Opening> openings = new ArrayList<>();
        FindIterable<Opening> iterable = getCollection().find(searchDoc);
        iterable.into(openings);
        return openings;
    }

    public Opening fetchOpening(String jobId) {
        Document searchDoc = new Document("_id", new ObjectId(jobId));
        return getCollection().find(searchDoc).first();
    }


}
