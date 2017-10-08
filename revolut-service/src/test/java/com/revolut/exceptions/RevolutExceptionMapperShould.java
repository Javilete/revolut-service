package com.revolut.exceptions;

import com.revolut.domain.model.ErrorResponse;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RevolutExceptionMapperShould {

    private static final String EXCEPTION_MESSAGE = "Not enough balance";

    @Test
    public void return_a_precondition_failed_response_when_not_enough_balance_exception_thrown() {
        RevolutExceptionMapper exceptionMapper = new RevolutExceptionMapper();
        NotEnoughBalanceException exception = new NotEnoughBalanceException(EXCEPTION_MESSAGE);

        Response response = exceptionMapper.toResponse(exception);

        assertThat(response.getStatus(), is(Response.Status.PRECONDITION_FAILED.getStatusCode()));
        assertThat(((ErrorResponse) response.getEntity()).getMessage(), is(EXCEPTION_MESSAGE));
    }
}
