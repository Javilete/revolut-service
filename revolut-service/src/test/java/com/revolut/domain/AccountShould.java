package com.revolut.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AccountShould {

    private static final String ACCOUNT_ID = UUID.randomUUID().toString();
    private static final double FIFTY_AMOUNT = 50.00;

    private Account account;

    @Before
    public void setUp() throws Exception {
        account = new Account(ACCOUNT_ID, new BigDecimal(100.00));
    }

    @Test
    public void add_amount_to_current_balance() {
        account.add(new BigDecimal(FIFTY_AMOUNT));

        assertThat(account.getBalance(), is(new BigDecimal(150.00)));
    }

    @Test
    public void decrease_amount_of_current_balance() {
        account.substract(new BigDecimal(FIFTY_AMOUNT));

        assertThat(account.getBalance(), is(new BigDecimal(FIFTY_AMOUNT)));
    }

    @Test
    public void return_true_when_it_does_have_enough_balance() {
        assertTrue(account.hasEnoughBalance(new BigDecimal(FIFTY_AMOUNT)));
    }

    @Test
    public void return_false_when_it_does_not_have_enough_balance() {
        assertFalse(account.hasEnoughBalance(new BigDecimal(100.01)));
    }

    @Test
    public void add_amount_to_an_account_by_multiple_threads_concurrently() throws InterruptedException {
        executeTask(10000, () -> account.add(BigDecimal.ONE));

        assertThat(account.getBalance(), is(new BigDecimal(10100.00)));
    }

    @Test
    public void substract_amount_of_an_account_by_multiple_threads_concurrently() throws InterruptedException {
        executeTask(100, () -> account.substract(BigDecimal.ONE));

        assertThat(account.getBalance(), is(new BigDecimal(0.00)));
    }

    private void executeTask(int endValue, Runnable task) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        IntStream.range(0, endValue).forEach(i -> executor.submit(task));
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
