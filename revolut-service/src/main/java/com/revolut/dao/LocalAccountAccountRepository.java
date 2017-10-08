package com.revolut.dao;

import com.revolut.domain.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LocalAccountAccountRepository implements AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();

    @Override
    public void create(Account account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public Optional<Account> fetchBy(String id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
