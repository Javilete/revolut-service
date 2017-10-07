package com.revolut.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class RevolutHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
