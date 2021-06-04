package com.redhat.idaas.datasynthesis.dtos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class UserIdentityWithType {
    @Schema(description = "Number of random records to be inserted to database")
    public int count;
    public short dataGenTypeId;
    public String domainValue;
    public String additionalDetail;

    public UserIdentityWithType() {

    }

    public UserIdentityWithType(int count, short typeId, String domainValue, String additionalDetail) {
        this.count = count;
        this.dataGenTypeId = typeId;
        this.domainValue = domainValue;
        this.additionalDetail = additionalDetail;
    }

    //toString
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
}