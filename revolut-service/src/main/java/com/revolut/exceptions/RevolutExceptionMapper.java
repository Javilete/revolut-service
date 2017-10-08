package com.revolut.exceptions;


import com.revolut.domain.model.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class RevolutExceptionMapper implements ExceptionMapper<NotEnoughBalanceException>{
    @Override
    public Response toResponse(NotEnoughBalanceException exception) {
        return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                .entity(new ErrorResponse(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
