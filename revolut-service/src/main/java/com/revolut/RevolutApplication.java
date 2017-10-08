package com.revolut;

import com.revolut.config.RevolutServiceConfiguration;
import com.revolut.dao.AccountRepository;
import com.revolut.dao.LocalAccountRepository;
import com.revolut.exceptions.RevolutExceptionMapper;
import com.revolut.healthcheck.RevolutHealthCheck;
import com.revolut.resources.AccountResource;
import com.revolut.services.AccountService;
import com.revolut.utils.Generator;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RevolutApplication extends Application<RevolutServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new RevolutApplication().run(args);
    }

    @Override
    public String getName() {
        return "Revolut-Application";
    }

    @Override
    public void initialize(Bootstrap<RevolutServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(RevolutServiceConfiguration revolutServiceConfiguration, Environment environment) throws Exception {
        RevolutHealthCheck healthCheck = new RevolutHealthCheck();

        AccountRepository localAccountRepository = new LocalAccountRepository();
        AccountService accountService = new AccountService(new Generator(), localAccountRepository);
        AccountResource accountResource = new AccountResource(accountService);

        environment.jersey().register(new RevolutExceptionMapper());
        environment.jersey().register(accountResource);
        environment.healthChecks().register("Revolut", healthCheck);
    }
}
