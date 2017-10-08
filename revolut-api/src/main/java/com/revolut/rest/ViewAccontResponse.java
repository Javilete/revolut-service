package com.revolut.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ViewAccontResponse {

    private static final int SCALE = 2;

    private final String id;
    private final double balance;

    @JsonCreator
    public ViewAccontResponse(@JsonProperty("id") String id,
                              @JsonProperty("balance")BigDecimal balance) {
        this.id = id;
        this.balance = balance.setScale(SCALE).doubleValue();
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }
}
