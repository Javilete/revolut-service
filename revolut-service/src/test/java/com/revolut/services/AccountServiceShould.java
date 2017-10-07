package com.revolut.services;

import com.revolut.dao.AccountRepository;
import com.revolut.domain.Account;
import com.revolut.domain.CreateAccountRequest;
import com.revolut.exceptions.NegativeBalanceException;
import com.revolut.utils.Generator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceShould {

    private static final String ACC_ID = UUID.randomUUID().toString();
    private static final String DESTINATION_ACC_ID = UUID.randomUUID().toString();
    private static final String NON_EXISTING_ACC_ID = UUID.randomUUID().toString();

    private static final Account ACCOUNT = new Account(ACC_ID, new BigDecimal("100.00"));
    private static final Account DESTINATION_ACCOUNT = new Account(DESTINATION_ACC_ID, new BigDecimal("50.00"));
    private static final BigDecimal AMOUNT = new BigDecimal("20.00");

    @Mock
    private AccountRepository localRepository;

    @Mock
    private Generator idGenerator;

    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService(idGenerator, localRepository);
        when(idGenerator.getRandomId()).thenReturn(ACC_ID);
    }

    @Test
    public void create_a_new_account() {
        Account account = new Account(ACC_ID, new BigDecimal("0.00"));
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();

        accountService.insert(createAccountRequest);

        verify(localRepository).create(account);
    }

    @Test
    public void create_a_new_account_with_initial_balance() {
        Account account = new Account(ACC_ID, new BigDecimal("100.00"));
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("100.00");

        accountService.insert(createAccountRequest);

        verify(localRepository).create(account);
    }

    @Test
    public void transfer_an_amount_from_origin_account_to_destination_account() {
        when(localRepository.getAccountBy(ACC_ID))
                .thenReturn(Optional.of(ACCOUNT));
        when(localRepository.getAccountBy(DESTINATION_ACC_ID))
                .thenReturn(Optional.of(DESTINATION_ACCOUNT));

        accountService.transfer(ACC_ID, DESTINATION_ACC_ID, AMOUNT);

        assertThat(localRepository.getAccountBy(ACC_ID).get().getBalance().doubleValue(), is(120.00));
        assertThat(localRepository.getAccountBy(DESTINATION_ACC_ID).get().getBalance().doubleValue(), is(30.00));
    }

    @Test(expected = NegativeBalanceException.class)
    public void throw_an_exception_when_account_balance_of_origin_account_is_below_zero() {
        when(localRepository.getAccountBy(ACC_ID))
                .thenReturn(Optional.of(new Account(ACC_ID, new BigDecimal("-100.00"))));
        when(localRepository.getAccountBy(DESTINATION_ACC_ID))
                .thenReturn(Optional.of(DESTINATION_ACCOUNT));

        accountService.transfer(ACC_ID, DESTINATION_ACC_ID, AMOUNT);
    }

    @Test(expected = NotFoundException.class)
    public void throw_not_found_exception_when_the_origin_account_is_not_found() {
        when(localRepository.getAccountBy(NON_EXISTING_ACC_ID))
                .thenReturn(Optional.empty());

        accountService.transfer(NON_EXISTING_ACC_ID, DESTINATION_ACC_ID, AMOUNT);
    }

    @Test(expected = NotFoundException.class)
    public void throw_not_found_exception_when_the_destination_account_is_not_found() {
        when(localRepository.getAccountBy(ACC_ID))
                .thenReturn(Optional.of(ACCOUNT));
        when(localRepository.getAccountBy(NON_EXISTING_ACC_ID))
                .thenReturn(Optional.empty());

        accountService.transfer(ACC_ID, NON_EXISTING_ACC_ID, AMOUNT);
    }
}
