package com.revolut.dao;

import com.revolut.domain.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AccountRepositoryShould {

    private AccountRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new AccountRepository();
    }

    @Test
    public void create_an_account_in_the_repository() {
        String accountId = UUID.randomUUID().toString();
        Account account = new Account(accountId, new BigDecimal("100.00"));

        repository.create(account);

        assertThat(repository.allAccounts().size(), is(1));
    }

    @Test
    public void fetch_an_account_from_the_repository() {
        String accId = UUID.randomUUID().toString();
        Account account = new Account(accId, new BigDecimal("00.00"));
        repository.create(account);

        assertThat(repository.getAccountBy(accId), is(Optional.of(account)));
    }
}
