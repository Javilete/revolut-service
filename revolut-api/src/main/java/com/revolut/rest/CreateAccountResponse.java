package com.revolut.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAccountResponse {

    private final String accId;

    @JsonCreator
    public CreateAccountResponse(@JsonProperty("accId") String accId) {
        this.accId = accId;
    }

    public String getAccId() {
        return accId;
    }
}
