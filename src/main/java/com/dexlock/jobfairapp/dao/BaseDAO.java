package com.dexlock.jobfairapp.dao;

import com.dexlock.jobfairapp.services.MongoService;
import com.mongodb.client.MongoCollection;

public abstract class BaseDAO<T> {
    protected static MongoService mongoService = new MongoService();

    public abstract MongoCollection<T> getCollection();

    public abstract void create(T object);
}