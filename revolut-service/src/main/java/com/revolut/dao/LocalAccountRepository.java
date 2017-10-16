package com.revolut.dao;

import com.revolut.domain.Account;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalAccountRepository implements AccountRepository {

    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void create(Account account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public Optional<Account> fetchBy(String id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
