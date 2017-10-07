package com.revolut.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferRequest {

    private String originId;
    private String destinationId;
    private BigDecimal amount;

    @JsonCreator
    public TransferRequest(@JsonProperty("originId") String originId,
                           @JsonProperty("destinationId") String destinationId,
                           @JsonProperty("amount") String amount) {
        this.originId = originId;
        this.destinationId = destinationId;
        this.amount = new BigDecimal(amount);
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
}
