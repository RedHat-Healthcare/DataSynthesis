package com.redhat.idaas.datasynthesis.apis;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.idaas.datasynthesis.dtos.AccountNumber;
import com.redhat.idaas.datasynthesis.dtos.TypeAndCount;
import com.redhat.idaas.datasynthesis.exception.DataSynthesisException;
import com.redhat.idaas.datasynthesis.services.AccountNumberService;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

@Path("/account-numbers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountNumberResource {
    @Inject
    AccountNumberService service;
    
    @GET
    public List<AccountNumber> getAccountNumbers(
        @Parameter(description = "number of random records to be retrieved") @QueryParam int count,  
        @Parameter(required = false, description = "optional") @QueryParam Short dataGenTypeId) {
            return service.retrieveRandomAccountNumbers(count, dataGenTypeId);
    }
    
    @POST
    @APIResponse(responseCode = "201")
    public Response generateAccountNumbers(TypeAndCount countBody) throws DataSynthesisException {
        service.generateAccountNumbers(countBody.count, countBody.dataGenTypeId);
        return Response.status(Status.CREATED).build();
    }
}
