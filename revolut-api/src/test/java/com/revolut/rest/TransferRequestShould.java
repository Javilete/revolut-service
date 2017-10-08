package com.revolut.rest;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;

public class TransferRequestShould {

    @Test
    public void return_false_when_amount_is_null() {
        TransferRequest transferRequest =
                new TransferRequest("originId", "destinationId", null);

        assertFalse(transferRequest.isAmountGreaterThanZero());
    }

    @Test
    public void return_false_when_amount_is_a_negative_value() {
        TransferRequest transferRequest =
                new TransferRequest("originId", "destinationId", new BigDecimal(-10.00));

        assertFalse(transferRequest.isAmountGreaterThanZero());
    }
}
