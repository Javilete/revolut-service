package com.revolut.resources;

import com.codahale.metrics.annotation.Timed;
import com.revolut.domain.Account;
import com.revolut.rest.CreateAccountRequest;
import com.revolut.rest.CreateAccountResponse;
import com.revolut.rest.TransferRequest;
import com.revolut.rest.ViewAccontResponse;
import com.revolut.services.AccountService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @Timed
    @POST
    public Response create(@Valid @NotNull CreateAccountRequest createAccountRequest) {
        final String accountId = accountService.insert(createAccountRequest);
        return Response.status(Response.Status.CREATED)
                .entity(new CreateAccountResponse(accountId))
                .build();
    }

    @Timed
    @Path("/transfer")
    @POST
    public Response create(@Valid TransferRequest transferRequest) {
        accountService.transfer(transferRequest.getOriginId(),
                transferRequest.getDestinationId(),
                transferRequest.getAmount());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Timed
    @Path("/{accId}")
    @GET
    public Response findById(@NotNull @PathParam("accId") String id) {
        Account account = accountService.findBy(id);
        return Response.status(Response.Status.OK)
                .entity(new ViewAccontResponse(account.getId(),
                        account.getBalance()))
                .build();
    }
}
