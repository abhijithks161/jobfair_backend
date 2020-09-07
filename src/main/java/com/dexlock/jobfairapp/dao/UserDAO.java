package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.LoginRequest;
import com.dexlock.jobfairapp.models.SignupRequest;
import com.dexlock.jobfairapp.models.User;
import com.dexlock.jobfairapp.resources.helpers.UserResourceHelper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO extends BaseDAO<User> {

    UserResourceHelper helper = new UserResourceHelper();

    public MongoCollection<User> getCollection() {
        return mongoService.getDB().getCollection(CollectionNames.USER_COLL, User.class);
    }

    public MongoCollection<Document> getTokenCollection() {
        return mongoService.getDB().getCollection(CollectionNames.TOKEN_COLL, Document.class);
    }


    public User getUserFromSignupReq(SignupRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserType(request.getUserType());
        user.setVerified(request.getVerified());
        return user;
    }

    public void create(User user) {
        getCollection().insertOne(user);
    }


    public boolean isDuplicateUser(SignupRequest request) {
        Document searchDoc = new Document("email", request.getEmail());
        FindIterable<User> findIterable = getCollection().find(searchDoc);
        ArrayList<User> users = new ArrayList<>();
        findIterable.into(users);
        System.out.println(users);
        if (users.size() != 0)
            return true;
        else
            return false;
    }

    public Document checkUserExists(LoginRequest request) {
        Document searchEmail = new Document("email", request.getEmail());
        ArrayList<User> users = new ArrayList<>();
        FindIterable<User> findIterable = getCollection().find(searchEmail);
        findIterable.into(users);
        if (users.size() > 0) {
            for (User user : users) {
                if (user.getPassword().equals(request.getPassword())) {
                    Document userDocument = new Document("userId", user.getId().toString());
                    Document document = getTokenCollection().find(userDocument).first();

                    if (document != null) {
                        System.out.println("Token already exists");
                    } else {
                        Document document1 = new Document("userId", user.getUserId().toString()).append("createdAt", new Date()).append("userType", user.getUserType());
                        getTokenCollection().insertOne(document1);
                        return document1;
                    }
                }
            }
        }
        return null;
    }


    public User fetchUser(String userId) {
        Document searchDoc = new Document("_id", new ObjectId(userId));
        return getCollection().find(searchDoc).first();
    }

    public List<User> fetchAll() {
        List<User> users = new ArrayList<>();
        FindIterable<User> findIterable = getCollection().find();
        findIterable.into(users);
        return users;
    }

    public boolean verifyUser(String id) {
        Document searchDoc = new Document("_id", new ObjectId(id));
        UpdateResult updateResult;
        User user = getCollection().find(searchDoc).first();
        if (user != null) {
            Document updateDoc = new Document("$set", new Document("verified", "yes"));
            updateResult = getCollection().updateOne(searchDoc, updateDoc);
        } else {
            Document updateDoc = new Document("$set", new Document("verified", "no"));
            updateResult = getCollection().updateOne(searchDoc, updateDoc);
        }
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;
    }
}



