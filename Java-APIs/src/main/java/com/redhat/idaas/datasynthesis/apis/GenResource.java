package com.redhat.idaas.datasynthesis.apis;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

    @GET
    @Path("ssn/{count}")
    public void generateSSNs(@PathParam int count) {
        ssnService.generateSSN(count);
    }

    @GET
    @Path("accountnumber/{count}")
    public void generateAccountNumbers(@PathParam int count) {
        throw new UnsupportedOperationException("generateAccountNumbers Not yet implemented");
    }

    @GET
    @Path("creditcard/{count}")
    public void generateCreditCards(@PathParam int count) {
        throw new UnsupportedOperationException("generateCreditCards Not yet implemented");
    }

    @GET
    @Path("birthdate/{count}")
    public void generateBirthDates(@PathParam int count) {
        throw new UnsupportedOperationException("generateBirthDates Not yet implemented");
    }

    @GET
    @Path("ein/{count}")
    public void generateEINs(@PathParam int count) {
        einService.generateEinNumber(count);
    }

    @GET
    @Path("phonenumber/{count}")
    public void generatePhoneNumbers(@PathParam int count) {
        throw new UnsupportedOperationException("generatePhoneNumbers Not yet implemented");
    }

    @GET
    @Path("useridentity/{count}")
    public void generateUserIdentities(@PathParam int count) {
        throw new UnsupportedOperationException("generateUserIdentities Not yet implemented");
    }
}
