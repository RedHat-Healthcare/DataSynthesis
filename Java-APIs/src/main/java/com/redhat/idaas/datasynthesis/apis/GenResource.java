package com.redhat.idaas.datasynthesis.apis;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.idaas.datasynthesis.services.AccountNumberService;
import com.redhat.idaas.datasynthesis.services.AddressService;
import com.redhat.idaas.datasynthesis.services.CreditCardService;
import com.redhat.idaas.datasynthesis.services.DateOfBirthService;
import com.redhat.idaas.datasynthesis.services.DriversLicenseNumberService;
import com.redhat.idaas.datasynthesis.services.EINService;
import com.redhat.idaas.datasynthesis.services.PhoneNumberService;
import com.redhat.idaas.datasynthesis.services.SSNService;
import com.redhat.idaas.datasynthesis.services.UserIdentityService;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/gen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenResource {

    @Inject
    SSNService ssnService;

    @Inject
    AccountNumberService accountNumberService;

    @Inject
    AddressService addressService;

    @Inject
    CreditCardService creditCardService;

    @Inject
    DateOfBirthService dobService;

    @Inject
    DriversLicenseNumberService dlnService;

    @Inject
    EINService einService;

    @Inject
    PhoneNumberService phoneNumberService;

    @Inject
    UserIdentityService userIdentityService;

    @POST
    @Path("ssn/{count}")
    public Response generateSSNs(@PathParam int count) {
        ssnService.generateSSN(count);
        return Response.status(Status.CREATED).build();
    }

    @POST
    @Path("accountnumber/{count}")
    public Response generateAccountNumbers(@PathParam int count) {
        throw new UnsupportedOperationException("generateAccountNumbers Not yet implemented");
    }

    @POST
    @Path("creditcard/{count}")
    public Response generateCreditCards(@PathParam int count) {
        throw new UnsupportedOperationException("generateCreditCards Not yet implemented");
    }

    @POST
    @Path("birthdate/{count}")
    public Response generateBirthDates(@PathParam int count) {
        throw new UnsupportedOperationException("generateBirthDates Not yet implemented");
    }

    @POST
    @Path("ein/{count}")
    public Response generateEINs(@PathParam int count) {
        einService.generateEinNumber(count);
        return Response.status(Status.CREATED).build();
    }

    @POST
    @Path("phonenumber/{count}")
    public Response generatePhoneNumbers(@PathParam int count) {
        phoneNumberService.generatePhoneNumber(count);
        return Response.status(Status.CREATED).build();
    }

    @POST
    @Path("useridentity/{count}")
    public Response generateUserIdentities(@PathParam int count) {
        throw new UnsupportedOperationException("generateUserIdentities Not yet implemented");
    }
}
