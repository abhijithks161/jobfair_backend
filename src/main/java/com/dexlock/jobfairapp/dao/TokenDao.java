package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.constants.CollectionNames;
import com.dexlock.jobfairapp.models.Token;
import com.dexlock.jobfairapp.models.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TokenDao extends BaseDAO<User> {

    @Override
    public MongoCollection getCollection() {
        return null;
    }

    @Override
    public void create(User object) {

    }


    public MongoCollection<Token> getTokenCollection() {
        return mongoService.getDB().getCollection(CollectionNames.TOKEN_COLL, Token.class);
    }


    public ObjectId getTokenWithId(ObjectId token) {
        Document searchToken = new Document("_id", token);
        List<Token> tokens = new ArrayList<>();
        FindIterable<Token> iterable = getTokenCollection().find(searchToken);
        iterable.into(tokens);
        if (tokens.size() > 0)
            return new ObjectId(tokens.get(0).getUserId());
        else
            return null;

    }

    public boolean deleteToken(String id) {
        Document searchDoc = new Document("userId", id);
        DeleteResult result = getTokenCollection().deleteOne(searchDoc);
        if (result.getDeletedCount() > 0)
            return true;
        else
            return false;
    }
}


