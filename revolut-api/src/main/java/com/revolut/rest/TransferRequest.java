package com.revolut.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

public class TransferRequest {

    @NotEmpty
    private String originId;

    @NotEmpty
    private String destinationId;

    private BigDecimal amount;

    @JsonCreator
    public TransferRequest(@JsonProperty("originId") String originId,
                           @JsonProperty("destinationId") String destinationId,
                           @JsonProperty("amount") BigDecimal amount) {
        this.originId = originId;
        this.destinationId = destinationId;
        this.amount = amount;
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @ValidationMethod(message = "amount not be greater than 0.00")
    @JsonIgnore
    public boolean isAmountGreaterThanZero() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
