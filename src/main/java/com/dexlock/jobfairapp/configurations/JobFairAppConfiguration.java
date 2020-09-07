package com.dexlock.jobfairapp.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class JobFairAppConfiguration extends Configuration {
    private static String mongoHost;
    private static Integer mongoPort;
    private static String mongoDBName;
    private static String uploadFolder;
    private static String uploadDir;
    private static String hostIp;
    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public static String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        JobFairAppConfiguration.hostIp = hostIp;
    }

    public static String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        JobFairAppConfiguration.uploadDir = uploadDir;
    }

    public static String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        JobFairAppConfiguration.uploadFolder = uploadFolder;
    }

    public static String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        JobFairAppConfiguration.mongoHost = mongoHost;
    }

    public static Integer getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(Integer mongoPort) {
        JobFairAppConfiguration.mongoPort = mongoPort;
    }

    public static String getMongoDBName() {
        return mongoDBName;
    }

    public void setMongoDBName(String mongoDBName) {
        JobFairAppConfiguration.mongoDBName = mongoDBName;
    }
}
