package com.revolut.dao;

import com.revolut.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {

    void create(Account account);

    Optional<Account> fetchBy(String id);
}
