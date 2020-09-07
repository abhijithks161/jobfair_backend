package com.dexlock.jobfairapp.resources;

import com.dexlock.jobfairapp.configurations.JobFairAppConfiguration;
import com.dexlock.jobfairapp.dao.JobSeekerDAO;
import com.dexlock.jobfairapp.models.*;
import com.dexlock.jobfairapp.models.api.APIResponse;
import com.dexlock.jobfairapp.resources.helpers.JobSeekerResourceHelper;
import io.dropwizard.jersey.PATCH;
import io.swagger.annotations.Api;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Api("JobSeeker")
@Path("/jobseeker")
@Produces(MediaType.APPLICATION_JSON)

public class JobSeekerResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSeekerResource.class);
    private JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
    private JobSeekerResourceHelper jobSeekerResourceHelper = new JobSeekerResourceHelper();


    @Path("/all")
    @GET
    public Response fetchAllUsers() {
        List<JobSeeker> jobSeekers = jobSeekerDAO.fetchAll();
        APIResponse apiResponse = new APIResponse();
        if (jobSeekers.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(jobSeekers);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("JobSeekers empty");
            return Response.ok().entity(apiResponse).build();
        }
    }


    @Path("/profile")
    @GET
    public Response viewProfile(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = jobSeekerDAO.fetchJobSeekerDetail(id);
        if (jobSeeker != null) {
            String ip = JobFairAppConfiguration.getHostIp();
            String uploadDir = JobFairAppConfiguration.getUploadDir();
            String filename = jobSeeker.getProfilePic();
            String url;
            if (filename != null)
                url = "http://" + ip + ":8000" + uploadDir + filename;
            else
                url = null;
            jobSeeker.setProfilePicURL(url);
            apiResponse.setStatus(1);
            apiResponse.setData(jobSeeker);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("jobseeker not found");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/edit")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editProfile(EditJobSeekerRequest request) {
        APIResponse apiResponse = new APIResponse();
        if (jobSeekerDAO.editJobSeeker(request)) {
            apiResponse.setStatus(1);
            apiResponse.setData(request);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("jobseeker not found");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/fetch-qualification")
    @GET
    public Response fetchQualification(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = jobSeekerDAO.fetchJobSeekerDetail(id);
        ArrayList<Qualification> qualifications = jobSeeker.getQualifications();
        if (qualifications != null) {
            apiResponse.setStatus(1);
            apiResponse.setData(qualifications);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("No Qualifications");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/edit-qualification")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addQualification(AddQualificationRequest request) {
        APIResponse apiResponse = new APIResponse();
        if (jobSeekerDAO.updateQualification(request)) {
            apiResponse.setStatus(1);
            apiResponse.setData(request);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("Failed to add qualification");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @QueryParam("id") String id,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
        APIResponse apiResponse = new APIResponse();
        String uploadedFileLocation = JobFairAppConfiguration.getUploadFolder() + fileDetail.getFileName();
        LOGGER.info(uploadedFileLocation);
        // save it
        jobSeekerDAO.writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;
        if (jobSeekerDAO.uploadImage(id, fileDetail.getFileName())) {
            apiResponse.setStatus(1);
            apiResponse.setData(output);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("failed to upload image");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/apply")
    @PATCH
    public Response registerJob(@QueryParam("jobId") String jobId,
                                @QueryParam("userId") String userId,
                                @QueryParam("jobFairId") String jobFairId) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = jobSeekerDAO.fetchJobSeekerDetail(userId);
        String email = jobSeeker.getEmail();
        if (jobSeekerResourceHelper.verify(jobSeeker.getEmail())) {
            String status = jobSeekerResourceHelper.registerJob(jobId, email);
            if (status.equals("applied")) {
                jobSeekerResourceHelper.updateAppliedTotal(jobId);
                jobSeekerResourceHelper.updatePerformance(jobFairId, userId);
                jobSeekerResourceHelper.updateEmployerPerformance(jobFairId, jobId);
                apiResponse.setStatus(1);
                return Response.ok().entity(apiResponse).build();
            } else if (status.equalsIgnoreCase("already applied")) {
                apiResponse.setStatus(0);
                apiResponse.setError("Already Applied");
                return Response.ok().entity(apiResponse).build();
            } else {
                apiResponse.setStatus(0);
                apiResponse.setError("No Vacancy");
                return Response.ok().entity(apiResponse).build();
            }
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("user not verified");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/performance")
    @GET
    public Response viewPerformance(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<JobSeekerPerformance> performance = jobSeekerResourceHelper.viewPerformance(id);

        performance.sort((jsp1, jsp2) -> {
            if (jsp1.getJobFairDate().getTime() < jsp2.getJobFairDate().getTime())
                return -1;
            else if (jsp1.getJobFairDate().getTime() == jsp2.getJobFairDate().getTime())
                return 0;
            else
                return 1;
        });
        if (performance.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(performance);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("not attended any jobfair");
            return Response.ok().entity(apiResponse).build();
        }
    }


    @Path("/matched-openings")
    @GET
    public Response fetch(@QueryParam("jobseekerId") String userId, @QueryParam("jobFairId") String jobFairId) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<Opening> openings = jobSeekerResourceHelper.fetchMatchedOpenings(userId, jobFairId);
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

    @Path("all-openings")
    @GET
    public Response fetchAllOpenings(@QueryParam("jobFairId") String jobFairId) {
        ArrayList<Opening> openings = jobSeekerResourceHelper.fetchAllOpenings(jobFairId);
        APIResponse apiResponse = new APIResponse();
        if (openings.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(openings);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("no openings");
            return Response.ok().entity(openings).build();
        }
    }


}
