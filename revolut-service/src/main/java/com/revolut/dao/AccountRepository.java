package com.revolut.dao;

import com.revolut.domain.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();

    public void create(Account account) {
        accounts.put(account.getId(), account);
    }

    public Optional<Account> getAccountBy(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Map<String, Account> allAccounts() {
        return this.accounts;
    }
}
