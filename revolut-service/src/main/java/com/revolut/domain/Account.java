package com.revolut.domain;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class Account {

    private final String id;
    private AtomicReference<BigDecimal> aRbalance;

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.aRbalance = new AtomicReference<>(balance);
    }

    public BigDecimal getBalance() {
        return aRbalance.get();
    }

    public String getId() {
        return id;
    }

    public void add(BigDecimal amount) {
        aRbalance.updateAndGet(balance -> balance.add(amount));
    }

    public void substract(BigDecimal amount) {
        aRbalance.updateAndGet(balance -> balance.subtract(amount));
    }

    public boolean hasEnoughBalance(BigDecimal amount) {
        return aRbalance.get().compareTo(amount) >= 0;
    }
}
