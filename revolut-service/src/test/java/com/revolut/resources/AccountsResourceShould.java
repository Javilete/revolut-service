package com.revolut.resources;

import com.revolut.model.AccountRequest;
import com.revolut.services.AccountService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountsResourceShould {

    private static AccountService accountService = mock(AccountService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AccountResource(accountService))
            .build();

    @Test
    public void create_an_account() {
        AccountRequest accountRequest = new AccountRequest("Javier");

        resources.client().target("/accounts")
                .request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));

        verify(accountService).insert(accountRequest);
    }
}
