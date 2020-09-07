package com.dexlock.jobfairapp.resources;

import com.dexlock.jobfairapp.applications.CrunchifyJavaMailExample;
import com.dexlock.jobfairapp.dao.EmployerDAO;
import com.dexlock.jobfairapp.dao.JobSeekerDAO;
import com.dexlock.jobfairapp.dao.TokenDao;
import com.dexlock.jobfairapp.dao.UserDAO;
import com.dexlock.jobfairapp.models.*;
import com.dexlock.jobfairapp.models.api.APIResponse;
import io.swagger.annotations.Api;
import org.bson.Document;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api("User")
@Produces(MediaType.APPLICATION_JSON)


@Path("/user")
public class UserResource {
    JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
    EmployerDAO employerDAO = new EmployerDAO();
    TokenDao tokenDao = new TokenDao();
    private UserDAO userDAO = new UserDAO();

    @Path("/sign-up")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(@Valid SignupRequest request) throws MessagingException {
        User user = userDAO.getUserFromSignupReq(request);
        APIResponse response = new APIResponse();
        if (userDAO.isDuplicateUser(request)) {
            response.setError("User already exists");
            response.setStatus(0);
            return Response.ok().entity(response).build();
        } else {
            CrunchifyJavaMailExample example = new CrunchifyJavaMailExample();
            if (user.getUserType().equals("jobseeker")) {
                JobSeeker jobSeeker = new JobSeeker();
                jobSeeker.setFirstName(user.getFirstName());
                jobSeeker.setLastName(user.getLastName());
                jobSeeker.setEmail(user.getEmail());
                jobSeeker.setPhoneNumber(user.getPhoneNumber());
                jobSeekerDAO.create(jobSeeker);
                jobSeeker = jobSeekerDAO.getCollection().find(new Document("email", user.getEmail())).first();
                user.setUserId(jobSeeker.getId());
                userDAO.create(user);
                example.generateAndSendEmail(user.getEmail(), user.getId().toHexString());
            } else {
                Employer employer = new Employer();
                employer.setLastName(user.getLastName());
                employer.setFirstName(user.getFirstName());
                employer.setEmail(user.getEmail());
                employer.setPhoneNumber(user.getPhoneNumber());
                employer.setVerified(user.getVerified());
                employerDAO.create(employer);
                employer = employerDAO.getCollection().find(new Document("email", user.getEmail())).first();
                user.setUserId(employer.getId());
                userDAO.create(user);
                example.generateAndSendEmail(user.getEmail(), user.getId().toHexString());
            }
            response.setStatus(1);
            return Response.ok().entity(response).build();
        }
    }

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        APIResponse apiResponse = new APIResponse();
        Document document = userDAO.checkUserExists(request);
        if (document != null) {
            apiResponse.setStatus(1);
            apiResponse.setData(document);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            apiResponse.setError("Password Don't match");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/verification")
    @GET
    public Response verify(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        if (userDAO.verifyUser(id)) {
            apiResponse.setStatus(1);
            ;
            apiResponse.setData("verified");
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            ;
            apiResponse.setError("not verified");
            return Response.ok().entity(apiResponse).build();
        }
    }

    @Path("/logout")
    @GET
    public Response logout(@QueryParam("id") String id) {
        APIResponse apiResponse = new APIResponse();
        if (tokenDao.deleteToken(id)) {
            apiResponse.setStatus(1);
            return Response.ok().entity(apiResponse).build();
        } else {
            apiResponse.setStatus(0);
            return Response.ok().entity(apiResponse).build();
        }
    }

}
