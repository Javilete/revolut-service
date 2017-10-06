package com.revolut.model;

import java.math.BigDecimal;

public class TransferRequest {

    private String originId;
    private String destinationId;
    private BigDecimal amount;

    public TransferRequest() {

    }

    public TransferRequest(String originId, String destinationId, String amount) {
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
