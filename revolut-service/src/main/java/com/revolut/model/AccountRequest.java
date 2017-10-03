package com.revolut.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRequest {

    private String name;

    public AccountRequest() {
        // Jackson deserialization
    }

    @JsonCreator
    public AccountRequest(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountRequest)) return false;

        AccountRequest that = (AccountRequest) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
