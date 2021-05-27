package com.redhat.idaas.datasynthesis.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.github.curiousoddman.rgxgen.RgxGen;
import com.redhat.idaas.datasynthesis.dtos.AccountNumber;
import com.redhat.idaas.datasynthesis.exception.DataSynthesisException;
import com.redhat.idaas.datasynthesis.models.DataGeneratedAccountNumbersEntity;
import com.redhat.idaas.datasynthesis.models.PlatformDataAttributesEntity;
import com.redhat.idaas.datasynthesis.models.RefDataApplicationEntity;
import com.redhat.idaas.datasynthesis.models.RefDataDataGenTypesEntity;
import com.redhat.idaas.datasynthesis.models.RefDataStatusEntity;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import liquibase.pro.packaged.eN;

@ApplicationScoped
public class AccountNumberService extends RandomizerService<DataGeneratedAccountNumbersEntity, AccountNumber> {

    @Override
    protected long count(Object... queryOpts) {
        if (queryOpts.length <= 1) {
            return DataGeneratedAccountNumbersEntity.count();
        }
        return DataGeneratedAccountNumbersEntity.count((String)queryOpts[0], Arrays.copyOfRange(queryOpts, 1, queryOpts.length));
    }

    @Override
    protected PanacheQuery<DataGeneratedAccountNumbersEntity> findAll(Object... queryOpts) {
        if (queryOpts.length <= 1) {
            return DataGeneratedAccountNumbersEntity.findAll();
        }
        return DataGeneratedAccountNumbersEntity.find((String)queryOpts[0], Arrays.copyOfRange(queryOpts, 1, queryOpts.length));
    }

    @Override
    protected AccountNumber mapEntityToDTO(DataGeneratedAccountNumbersEntity e) {
        return new AccountNumber(e.getAccountNumberValue());
    }
 
    public List<AccountNumber> retrieveRandomAccountNumbers(int count, String typeName) {
        if (typeName == null) {
            return retrieveRandomData(count);
        }
        
        PlatformDataAttributesEntity dataAttribute = PlatformDataAttributesEntity.findByDataAttributeName("Account Numbers");
        RefDataDataGenTypesEntity dataType = RefDataDataGenTypesEntity.find("dataAttribute = ?1 and dataGenTypeDescription = ?2", dataAttribute, typeName).firstResult();
        return retrieveRandomData(count, "DataGenTypeID", dataType);
    }

    @Transactional
    public List<DataGeneratedAccountNumbersEntity> generateAccountNumbers(int count, String typeName) throws DataSynthesisException {
        List<DataGeneratedAccountNumbersEntity> results = new ArrayList<DataGeneratedAccountNumbersEntity>(count);
        RefDataApplicationEntity app = getRegisteredApp();
        RefDataStatusEntity defaultStatus = getDefaultStatus();
        Timestamp createdDate = new Timestamp(System.currentTimeMillis());

        PlatformDataAttributesEntity dataAttribute = PlatformDataAttributesEntity.findByDataAttributeName("Account Numbers");
        List<RefDataDataGenTypesEntity> allTypes = null;
        if (typeName != null) {
            RefDataDataGenTypesEntity dataType = RefDataDataGenTypesEntity.find("dataAttribute = ?1 and dataGenTypeDescription = ?2", dataAttribute, typeName).firstResult();
            allTypes = new ArrayList<RefDataDataGenTypesEntity>();
            allTypes.add(dataType);
        } else {
            allTypes = RefDataDataGenTypesEntity.find("dataAttribute", dataAttribute).list();
        }
        RgxGen[] rgxGens = new RgxGen[allTypes.size()];

        for (int i = 0; i < count;) {
            int selected = rand.nextInt(allTypes.size());
            RefDataDataGenTypesEntity dataType = allTypes.get(selected);
            RgxGen rgxGen = rgxGens[selected];
            if (rgxGen == null) {
                rgxGen = new RgxGen(dataType.getDefinition());
                rgxGens[selected] = rgxGen;
            }

            DataGeneratedAccountNumbersEntity entity = new DataGeneratedAccountNumbersEntity();
            entity.setCreatedDate(createdDate);
            entity.setStatus(defaultStatus);
            entity.setRegisteredApp(app);
            entity.setAccountNumberValue(rgxGen.generate(rand));
            entity.setDataGenType(dataType);

            if (entity.safePersist()) {
                results.add(entity);
                i++;
            }
        }

        return results;
    }
}
