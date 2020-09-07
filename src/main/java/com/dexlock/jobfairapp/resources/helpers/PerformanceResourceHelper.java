package com.dexlock.jobfairapp.resources.helpers;

import com.dexlock.jobfairapp.dao.JobFairDAO;
import com.dexlock.jobfairapp.models.JobFair;

public class PerformanceResourceHelper {
    JobFairDAO jobFairDAO = new JobFairDAO();

    public JobFair fetchJobFair(String id) {
        return jobFairDAO.fetchJobFairDetail(id);
    }
}
