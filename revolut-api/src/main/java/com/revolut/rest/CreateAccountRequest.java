package com.revolut.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Optional;

public class CreateAccountRequest {

    private BigDecimal balance;

    public CreateAccountRequest() {
        //Jackson deserialization
    }

    @JsonCreator
    public CreateAccountRequest(double balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    @JsonProperty
    public Optional<BigDecimal> getBalance() {
        return Optional.ofNullable(balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateAccountRequest)) return false;

        CreateAccountRequest that = (CreateAccountRequest) o;

        return balance != null ? balance.equals(that.balance) : that.balance == null;

    }

    @Override
    public int hashCode() {
        return balance != null ? balance.hashCode() : 0;
    }
}
