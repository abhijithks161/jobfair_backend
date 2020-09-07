package com.dexlock.jobfairapp.resources;

import com.dexlock.jobfairapp.configurations.JobFairAppConfiguration;
import com.dexlock.jobfairapp.dao.EmployerDAO;
import com.dexlock.jobfairapp.models.*;
import com.dexlock.jobfairapp.models.api.APIResponse;
import com.dexlock.jobfairapp.resources.helpers.EmployerResourceHelper;
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

@Api("Employer")
@Path("/employer")
@Produces(MediaType.APPLICATION_JSON)
public class EmployerResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSeekerResource.class);
    EmployerDAO employerDAO = new EmployerDAO();
    EmployerResourceHelper helper = new EmployerResourceHelper();


    @Path("/all")
    @GET
    public Response fetchAllUsers() {
        APIResponse apiResponse = new APIResponse();
        List<Employer> employers = employerDAO.fetchAll();
        if (employers.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(employers);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("Employers empty");
            return Response.ok().entity(apiResponse).build();
        }
    }


    @Path("/edit")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editProfile(EditEmployerRequest request) {
        APIResponse apiResponse = new APIResponse();
        if (employerDAO.editEmployer(request)) {
            apiResponse.setStatus(1);
            apiResponse.setData(request);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("failed to update");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/profile")
    @GET
    public Response viewProfile(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        Employer employer = employerDAO.fetchEmployerDetail(id);
        String ip = JobFairAppConfiguration.getHostIp();
        if (employer != null) {
            String uploadDir = JobFairAppConfiguration.getUploadDir();
            String filename = employer.getLogo();
            String url;
            if (filename != null)
                url = "http://" + ip + ":8000" + uploadDir + filename;
            else
                url = null;
            employer.setLogoUrl(url);
            apiResponse.setStatus(1);
            apiResponse.setData(employer);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("Employer not found");
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
        employerDAO.writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;
        if (employerDAO.uploadImage(id, fileDetail.getFileName())) {
            apiResponse.setStatus(1);
            apiResponse.setData(output);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("failed to upload image");
            return Response.ok().entity(apiResponse).build();
        }
    }


    @Path("/employer-logo")
    @GET
    public Response getLogo() {
        APIResponse apiResponse = new APIResponse();
        List<Employer> employers = employerDAO.fetchAll();
        List<String> logoUrl = new ArrayList<String>();
        for (Employer employer : employers) {
            String logo = employer.getLogoUrl();
            logoUrl.add(logo);
        }
        if (logoUrl != null) {
            apiResponse.setStatus(1);
            apiResponse.setData(logoUrl);
            return Response.ok().entity(logoUrl).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("Employer logo empty");
            return Response.ok().entity(logoUrl).build();
        }
    }

    @Path("/create-jobfair")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createJobFair(CreateJobFairRequest request) {
        APIResponse apiResponse = new APIResponse();
        JobFair jobFair = new JobFair();
        jobFair.setDate(request.getDate());
        jobFair.setTime(request.getTime());
        jobFair.setVenue(request.getVenue());
        jobFair.setLocation(request.getLocation());
        jobFair.setParticipatingCompanies(request.getParticipatingCompany());
        helper.createFair(jobFair);
        apiResponse.setData(jobFair);
        return Response.ok().entity(apiResponse).build();
    }

    @Path("/join-jobfair")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response joinJobFair(JoinJobFairRequest request) {
        APIResponse apiResponse = new APIResponse();
        if (helper.joinFair(request)) {
            apiResponse.setStatus(1);
            apiResponse.setData(request);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("failed");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/add-openings")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOpenings(AddOpeningRequest request) {
        APIResponse apiResponse = new APIResponse();
        if (helper.addOpenings(request)) {
            apiResponse.setStatus(1);
            ;
            apiResponse.setData(request);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            ;
            apiResponse.setError("failed to add");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/appeared")
    @PATCH
    public Response appeared(@QueryParam("jobFairId") String jobFairId,
                             @QueryParam("jobId") String jobId,
                             @QueryParam("userId") String userId) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = helper.fetchJobSeekerDetail(userId);
        String email = jobSeeker.getEmail();
        if (helper.apearedForJob(jobId, email)) {
            helper.updatePerformanceAppeared(jobFairId, userId);
            helper.updateEmployerPerformanceAppeared(jobFairId, jobId);
            apiResponse.setStatus(1);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/selected")
    @PATCH
    public Response selected(@QueryParam("jobFairId") String jobFairId,
                             @QueryParam("jobId") String jobId,
                             @QueryParam("userId") String userId) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = helper.fetchJobSeekerDetail(userId);
        String email = jobSeeker.getEmail();
        if (helper.selectedForJob(jobId, email)) {
            helper.updatePerformanceSelected(jobFairId, userId);
            helper.updateEmployerPerformanceSelected(jobFairId, jobId);
            apiResponse.setStatus(1);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            return Response.ok().entity(apiResponse).build();
        }
    }


    @Path("/spot-reg")
    @POST
    public Response spotRegistration(@QueryParam("jobFairId") String jobFairId,
                                     @QueryParam("jobId") String jobId,
                                     @QueryParam("userEmail") String userEmail) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = helper.fetchJobSeekerDetailWithEmail(userEmail);
        String status = helper.spotRegistration(jobFairId, jobId, userEmail);
        if (status.equals("applied")) {
            helper.updateAppliedTotal(jobId);
            helper.updatePerformance(jobFairId, jobSeeker.getId());
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

    }

    @Path("/fetch-jobseeker-detail")
    @POST
    public Response fetchJobseekerDetail(String email) {
        APIResponse apiResponse = new APIResponse();
        JobSeeker jobSeeker = helper.fetchJobSeekerDetailWithEmail(email);
        if (jobSeeker != null) {
            String ip = JobFairAppConfiguration.getHostIp();
            String uploadDir = JobFairAppConfiguration.getUploadDir();
            String filename = jobSeeker.getProfilePic();
            String url = "http://" + ip + ":8000" + uploadDir + filename;
            jobSeeker.setProfilePicURL(url);
            apiResponse.setStatus(1);
            apiResponse.setData(jobSeeker);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("jobSeeker not found");
            return Response.ok().entity(apiResponse).build();
        }

    }

    @Path("/delete-opening")
    @DELETE
    public Response deleteOpening(@QueryParam("jobFairId") String jobFairId,
                                  @QueryParam("jobId") String jobId,
                                  @QueryParam("email") String email) {
        APIResponse apiResponse = new APIResponse();
        if (helper.deleteOpening(jobFairId, jobId, email)) {
            apiResponse.setStatus(1);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("failed to delete");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/performance")
    @GET
    public Response viewPerformance(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<EmployerPerformance> performance = helper.viewPerformance(id);
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


    @Path("/applied-candidates")
    @GET
    public Response viewAppliedCandidates(@QueryParam("jobId") String jobId) {
        APIResponse apiResponse = new APIResponse();
        ArrayList<JobSeeker> jobSeekers = helper.viewAppliedCandidates(jobId);
        if (jobSeekers.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(jobSeekers);
            return Response.ok().entity(jobSeekers).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("No applied candidates");
            return Response.ok().entity(jobSeekers).build();
        }
    }

    @Path("/company-description")
    @POST
    public Response fetchDescription(CompanyDescRequest request) {
        APIResponse apiResponse = new APIResponse();
        List<String> descriptions = employerDAO.fetchDescription(request.getEmails());
        if (descriptions.size() > 0) {
            apiResponse.setStatus(1);
            apiResponse.setData(descriptions);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("description empty");
            return Response.ok().entity(apiResponse).build();
        }
    }

}
