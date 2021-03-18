package com.redhat.idaas.datasynthesis.services;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.redhat.idaas.datasynthesis.dtos.PhoneNumber;
import com.redhat.idaas.datasynthesis.exception.DataSynthesisException;
import com.redhat.idaas.datasynthesis.models.DataGeneratedPhoneNumberEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestTransaction
public class PhoneNumberServiceTest {
    @Inject
    PhoneNumberService service;

    @Test
    @Transactional
    public void testPhoneNumberGeneration() throws DataSynthesisException {
        DefaultApplication.seed();
        List<DataGeneratedPhoneNumberEntity> list = service.generatePhoneNumber(10);
        Assertions.assertEquals(10, list.size());

        Assertions.assertEquals(10, DataGeneratedPhoneNumberEntity.count());
    }

    @Test
    public void testPhoneNumberRandomizerHappyPath() throws Exception {
        testRandomizer(500000l, 1534, 1534);
    }

    @Test
    public void testPhoneNumberRandomizerMaxOut() throws Exception {
        testRandomizer(500000l, 10000, 5000);
    }

    @Test
    public void testPhoneNumberRandomizerLowRecordCount() throws Exception {
        testRandomizer(50L, 4321, 50);
    }

    private void testRandomizer(long databaseCount, int retrieveCount, int returnedCount) throws Exception {
        PanacheMock.mock(DataGeneratedPhoneNumberEntity.class);
        Mockito.when(DataGeneratedPhoneNumberEntity.count()).thenReturn(databaseCount);
        PanacheQuery allQuery = Mockito.mock(PanacheQuery.class);
        Mockito.when(DataGeneratedPhoneNumberEntity.findAll()).thenReturn(allQuery);
        Mockito.when(allQuery.page(Mockito.anyInt(), Mockito.anyInt())).thenReturn(allQuery);
        Mockito.when(allQuery.firstResult()).thenAnswer(invocation -> {
            Random r = new Random();
            DataGeneratedPhoneNumberEntity entity = new DataGeneratedPhoneNumberEntity();
            entity.setPhoneNumberId(r.nextLong());
            return entity;
        });
        
        List<PhoneNumber> randomPhoneNumbers = service.retrieveRandomPhoneNumbers(retrieveCount);
        Assertions.assertEquals(returnedCount, randomPhoneNumbers.size());
    }
}
