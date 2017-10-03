package com.revolut.resources;

import com.codahale.metrics.annotation.Timed;
import com.revolut.model.AccountRequest;
import com.revolut.services.AccountService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

    @POST
    @Timed
    public Response create(@Valid @NotNull AccountRequest accountRequest) {
        final String accountId = accountService.insert(accountRequest);
        return Response.status(Response.Status.CREATED).entity(accountId).build();
    }
}
