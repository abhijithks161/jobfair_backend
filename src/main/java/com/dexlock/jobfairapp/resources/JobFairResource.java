package com.dexlock.jobfairapp.resources;

import com.dexlock.jobfairapp.dao.JobFairDAO;
import com.dexlock.jobfairapp.models.JobFair;
import com.dexlock.jobfairapp.models.Opening;
import com.dexlock.jobfairapp.models.api.APIResponse;
import com.dexlock.jobfairapp.resources.helpers.JobFairResourceHelper;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Api("JobFair")
@Path("/jobFair")
@Produces(MediaType.APPLICATION_JSON)
public class JobFairResource {
    JobFairDAO jobFairDAO = new JobFairDAO();
    JobFairResourceHelper helper = new JobFairResourceHelper();


    @Path("/all")
    @GET
    public Response fetchAllJobFair(@QueryParam("page") int page, @QueryParam("count") int count) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<JobFair> jobFairs = jobFairDAO.fetchAllJobFair(page, count);
        if (jobFairs.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(jobFairs);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("no upcoming Jobfairs");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/fetch")
    @GET
    public Response fetchOneJobFair(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        JobFair jobFair = jobFairDAO.fetchJobFairDetail(id);
        if (jobFair != null) {
            apiResponse.setStatus(1);
            apiResponse.setData(jobFair);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("JobFair not found");
            return Response.ok().entity(apiResponse).build();
        }

    }

    @Path("/view-openings")
    @GET
    public Response viewOpenings(@QueryParam("jobFairId") String jobFairId, @QueryParam("email") String email) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<Opening> openings = jobFairDAO.viewOpenings(jobFairId, email);
        if (openings.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(openings);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("no openings");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/count")
    @GET
    public Response totalJobFairCount() {
        APIResponse apiResponse = new APIResponse();
        ArrayList<JobFair> jobFairs = jobFairDAO.fetchAll();
        int count = jobFairs.size();
        apiResponse.setStatus(1);
        apiResponse.setData(count);
        return Response.ok().entity(apiResponse).build();
    }


    @Path("/search")
    @POST
    public Response searchOpenings(Opening opening) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<Opening> openings = helper.searchOpenings(opening);
        if (openings.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(openings);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("NO matched openings");
            return Response.ok().entity(apiResponse).build();
        }

    }

}
