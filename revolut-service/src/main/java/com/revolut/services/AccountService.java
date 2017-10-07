package com.revolut.services;

import com.revolut.dao.AccountRepository;
import com.revolut.domain.Account;
import com.revolut.domain.CreateAccountRequest;
import com.revolut.exceptions.NegativeBalanceException;
import com.revolut.utils.Generator;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;

public class AccountService {

    private static final String INITIAL_BALANCE = "0.00";

    private Generator idGenerator;
    private AccountRepository accountRepository;

    public AccountService(Generator idGenerator, AccountRepository accountRepository) {
        this.idGenerator = idGenerator;
        this.accountRepository = accountRepository;
    }

    public String insert(CreateAccountRequest createAccountRequest) {
        String accIdentifier = idGenerator.getRandomId();
        Account account = new Account(accIdentifier, createAccountRequest.getBalance()
                .orElse(new BigDecimal(INITIAL_BALANCE)));
        accountRepository.create(account);

        return accIdentifier;
    }

    public Account findBy(String accId) {
        return accountRepository.fetchBy(accId)
                .orElseThrow(() -> new NotFoundException("No account found with id: " + accId));
    }

    public void transfer(String originAccId, String destinationAccId, BigDecimal amount) {
        Account originAcc = this.findBy(originAccId);
        if (!originAcc.hasEnoughBalance(amount)) {
            throw new NegativeBalanceException();
        }

        this.findBy(destinationAccId).decrease(amount);
        originAcc.add(amount);
    }
}
