package com.redhat.idaas.datasynthesis.services;

import java.sql.Timestamp;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.github.curiousoddman.rgxgen.RgxGen;
import com.redhat.idaas.datasynthesis.dtos.DLN;
import com.redhat.idaas.datasynthesis.exception.DataSynthesisException;
import com.redhat.idaas.datasynthesis.models.DataGeneratedDriversLicensesEntity;
import com.redhat.idaas.datasynthesis.models.PlatformDataAttributesEntity;
import com.redhat.idaas.datasynthesis.models.RefDataApplicationEntity;
import com.redhat.idaas.datasynthesis.models.RefDataDataGenTypesEntity;
import com.redhat.idaas.datasynthesis.models.RefDataStatusEntity;
import com.redhat.idaas.datasynthesis.models.RefDataUsStatesEntity;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

@ApplicationScoped
public class DriversLicenseNumberService extends RandomizerService<DataGeneratedDriversLicensesEntity, DLN> {
    // format is based on https://www.mvrdecoder.com/content/drvlicformats.aspx
    // https://ntsi.com/drivers-license-format/ is outdated
    //
    // Placeholder # num, . optional num, * num or letter, % letter
    private static final Map<String, String> FORMAT_MAP = Stream.of(new SimpleEntry<>("AL", "#######."), 
            new SimpleEntry<>("AK", "#######"), new SimpleEntry<>("AZ", "%########"),
            new SimpleEntry<>("AR", "#########"), new SimpleEntry<>("CA", "%#######"),
            new SimpleEntry<>("CO", "#########"), new SimpleEntry<>("CT", "#########"),
            new SimpleEntry<>("DE", "#......"), new SimpleEntry<>("DC", "#######"),
            new SimpleEntry<>("FL", "%############"), new SimpleEntry<>("GA", "#########"),
            new SimpleEntry<>("HI", "H########"), new SimpleEntry<>("ID", "%%%######"),
            new SimpleEntry<>("IL", "%###########"), new SimpleEntry<>("IN", "##########"),
            new SimpleEntry<>("IA", "###%%####"), new SimpleEntry<>("KS", "K########"),
            new SimpleEntry<>("KY", "%########"), new SimpleEntry<>("LA", "0########"),
            new SimpleEntry<>("ME", "#######"), new SimpleEntry<>("MD", "%############"),
            new SimpleEntry<>("MA", "S########"), new SimpleEntry<>("MI", "%############"),
            new SimpleEntry<>("MN", "%############"), new SimpleEntry<>("MS", "#########"),
            new SimpleEntry<>("MO", "%#########"), new SimpleEntry<>("MT", "#############"),
            new SimpleEntry<>("NE", "%########"), new SimpleEntry<>("NV", "############"),
            new SimpleEntry<>("NH", "%%%#########"), new SimpleEntry<>("NJ", "%##############"),
            new SimpleEntry<>("NM", "#########"), new SimpleEntry<>("NY", "#########"),
            new SimpleEntry<>("NC", "############"), new SimpleEntry<>("ND", "%%%######"),
            new SimpleEntry<>("OH", "%%######"), new SimpleEntry<>("OK", "%#########"),
            new SimpleEntry<>("OR", "%######."), new SimpleEntry<>("PA", "########"),
            new SimpleEntry<>("RI", "[V#]######"), new SimpleEntry<>("SC", "######....."),
            new SimpleEntry<>("SD", "########."), new SimpleEntry<>("TN", "#######.."),
            new SimpleEntry<>("TX", "########"), new SimpleEntry<>("UT", "####......"),
            new SimpleEntry<>("VT", "#######[A#]"), new SimpleEntry<>("VA", "%########"),
            new SimpleEntry<>("WA", "%%%%%###**"), new SimpleEntry<>("WV", "#######%*#####"),
            new SimpleEntry<>("WI", "%#############"), new SimpleEntry<>("WY", "#########"))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    private static final List<Map.Entry<String, String>> FORMAT_LIST = FORMAT_MAP.entrySet().stream()
            .collect(Collectors.toList());

    @Override
    protected long count(Object... queryOpts) {
        if (queryOpts.length <= 1) {
            return DataGeneratedDriversLicensesEntity.count();
        }
        return DataGeneratedDriversLicensesEntity.count((String) queryOpts[0],
                Arrays.copyOfRange(queryOpts, 1, queryOpts.length));
    }

    @Override
    protected PanacheQuery<DataGeneratedDriversLicensesEntity> findAll(Object... queryOpts) {
        if (queryOpts.length <= 1) {
            return DataGeneratedDriversLicensesEntity.findAll();
        }
        return DataGeneratedDriversLicensesEntity.find((String) queryOpts[0],
                Arrays.copyOfRange(queryOpts, 1, queryOpts.length));
    }

    @Override
    protected DLN mapEntityToDTO(DataGeneratedDriversLicensesEntity e) {
        return new DLN(e.getDln(), e.getState().getStateId(), null);
    }

    @Transactional
    public List<DataGeneratedDriversLicensesEntity> generatedDriverLicenses(int count, String state)
            throws DataSynthesisException {
        List<DataGeneratedDriversLicensesEntity> results = new ArrayList<DataGeneratedDriversLicensesEntity>();
        RefDataApplicationEntity app = getRegisteredApp();
        RefDataStatusEntity defaultStatus = getDefaultStatus();
        Timestamp createdDate = new Timestamp(System.currentTimeMillis());

        PlatformDataAttributesEntity dlnDataAttribute = PlatformDataAttributesEntity.findByDataAttributeName("Drivers License Number");
        List<RefDataDataGenTypesEntity> dlnTypes = null;
        RefDataUsStatesEntity stateEntity = null;
        RgxGen rgxGen = null;
        if (state != null) {
            stateEntity = RefDataUsStatesEntity.findById(state);
            RefDataDataGenTypesEntity dataType = RefDataDataGenTypesEntity.find("dataAttribute = ?1 and dataGenTypeDescription = ?2", dlnDataAttribute, state).firstResult();
            rgxGen = new RgxGen(dataType.getDefinition());
        } else {
            dlnTypes = RefDataDataGenTypesEntity.find("dataAttribute", dlnDataAttribute).list();
        }

        for (int i = 0; i < count;) {
            DataGeneratedDriversLicensesEntity entity = new DataGeneratedDriversLicensesEntity();
            entity.setCreatedDate(createdDate);
            entity.setStatus(defaultStatus);
            entity.setRegisteredApp(app);
            if (state == null) {
                // generate a random DLN for a random state
                RefDataDataGenTypesEntity dataType = dlnTypes.get(rand.nextInt(dlnTypes.size()));
                entity.setState(RefDataUsStatesEntity.findById(dataType.getDataGenTypeDescription()));
                rgxGen = new RgxGen(dataType.getDefinition());
                entity.setDln(rgxGen.generate(rand));
            } else {
                entity.setState(stateEntity);
                entity.setDln(rgxGen.generate(rand));
            }
            if (entity.safePersist()) {
                results.add(entity);
                i++;
            }
        }

        return results;
    }

    public List<DLN> retrieveRandomDriverLicenses(int count, String state) {
        if (state == null) {
            return retrieveRandomData(count);
        } 
        
        return retrieveRandomData(count, "StateCode", state);
    }
}