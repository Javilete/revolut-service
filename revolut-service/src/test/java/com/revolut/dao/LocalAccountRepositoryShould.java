package com.revolut.dao;

import com.revolut.domain.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LocalAccountRepositoryShould {

    private static final String ACCOUNT_ID = UUID.randomUUID().toString();
    private static final Account ACCOUNT = new Account(ACCOUNT_ID, new BigDecimal("100.00"));

    private AccountRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new LocalAccountRepository();
    }

    @Test
    public void create_an_account_in_the_repository() {
        repository.create(ACCOUNT);

        assertThat(repository.fetchBy(ACCOUNT_ID), is(Optional.of(ACCOUNT)));
    }
}
