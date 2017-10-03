package com.revolut.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class RevolutHealthCheck extends HealthCheck {

    private final String applicationContextPath;

    public RevolutHealthCheck(String applicationContextPath) {
        this.applicationContextPath = applicationContextPath;
    }

    @Override
    protected Result check() throws Exception {
        throw new UnsupportedOperationException();

    }
}
