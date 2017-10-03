package com.revolut;

import com.revolut.config.RevolutServiceConfiguration;
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
        throw new UnsupportedOperationException();
    }
}
