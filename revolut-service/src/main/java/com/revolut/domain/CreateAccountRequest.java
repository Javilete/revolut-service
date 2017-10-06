package com.revolut.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountRequest {

    private BigDecimal balance;

    public AccountRequest() {
        // Jackson deserialization
    }

    @JsonCreator
    public AccountRequest(String balance) {
        this.balance = new BigDecimal(balance);
    }

    @JsonProperty
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountRequest)) return false;

        AccountRequest that = (AccountRequest) o;

        return balance != null ? balance.equals(that.balance) : that.balance == null;

    }

    @Override
    public int hashCode() {
        return balance != null ? balance.hashCode() : 0;
    }
}
