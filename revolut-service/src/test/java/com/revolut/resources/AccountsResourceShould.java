package com.revolut.resources;

import com.revolut.domain.rest.CreateAccountRequest;
import com.revolut.domain.rest.TransferRequest;
import com.revolut.services.AccountService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("100.00");

        Response response = resources.client().target("/accounts")
                .request()
                .post(Entity.entity(createAccountRequest, MediaType.APPLICATION_JSON));

        verify(accountService).insert(createAccountRequest);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
    }

    @Test
    public void transfer_an_amount_from_origin_to_destination_account() {
        String originAcc = UUID.randomUUID().toString();
        String destinationAcc = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal("100.00");

        TransferRequest transferRequest = new TransferRequest(originAcc, destinationAcc, "100.00");
        Response response = resources.client().target("/accounts/transfer")
                .request()
                .post(Entity.entity(transferRequest, MediaType.APPLICATION_JSON));

        verify(accountService).transfer(originAcc, destinationAcc, amount);
        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    public void fetch_an_existing_account() {
        String accId = UUID.randomUUID().toString();

        Response response = resources.client().target("/accounts/" + accId)
                .request()
                .get();

        verify(accountService).findBy(accId);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}
