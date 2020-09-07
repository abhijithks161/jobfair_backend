package com.dexlock.jobfairapp.applications;

import com.dexlock.jobfairapp.applications.filters.CORSFilter;
import com.dexlock.jobfairapp.configurations.JobFairAppConfiguration;
import com.dexlock.jobfairapp.models.User;
import com.dexlock.jobfairapp.resources.EmployerResource;
import com.dexlock.jobfairapp.resources.JobFairResource;
import com.dexlock.jobfairapp.resources.JobSeekerResource;
import com.dexlock.jobfairapp.resources.UserResource;
import com.dexlock.jobfairapp.resources.helpers.UserAuthenticator;
import com.dexlock.jobfairapp.services.MongoService;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

//import com.dexlock.jobfairapp.resources.helpers.UserAuthenticator;

public class MainApp extends Application<JobFairAppConfiguration> {
    public static void main(String[] args) throws Exception {
        new MainApp().run(new String[]{"server", "conf.yml"});
    }

    @Override
    public void initialize(Bootstrap<JobFairAppConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<JobFairAppConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(JobFairAppConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(JobFairAppConfiguration jobFairAppConfiguration, Environment environment) throws Exception {
        environment.lifecycle().manage(new MongoService());
        environment.jersey().register(new UserResource());
        environment.jersey().register(new JobSeekerResource());
        environment.jersey().register(new EmployerResource());
        environment.jersey().register(new JobFairResource());
        environment.jersey().register(MultiPartFeature.class);
        environment.servlets()
                .addFilter("CORSFilter", new CORSFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new UserAuthenticator())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

    }

}

