package eu.europa.ec.joinup.tsl.business.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TLRepositoryTest extends AbstractSpringTest {

    @Autowired
    private TLRepository repo;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void findByTerritoryAndStatusAndArchiveTrueOrderByIssueDateDesc() {
        DBCountries territory = createEurope();

        int createTLinDB = createTLinDB(false, territory);
        assertTrue(createTLinDB > 0);

        DBTrustedLists findOne = repo.findOne(createTLinDB);
        assertNotNull(findOne);
        assertNotNull(findOne.getTerritory());
        assertEquals(territory.getCodeTerritory(), findOne.getTerritory().getCodeTerritory());

        assertTrue(CollectionUtils.isEmpty(repo.findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(territory, TLStatus.PROD)));

        createTLinDB = createTLinDB(true, territory);
        assertTrue(createTLinDB > 0);

        assertTrue(CollectionUtils.isNotEmpty(repo.findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(territory, TLStatus.PROD)));

    }

    private DBCountries createEurope() {
        DBCountries territory = new DBCountries();
        territory.setCodeTerritory("EU");
        territory.setCountryName("europa");
        return countryRepository.save(territory);
    }

    private int createTLinDB(boolean archive, DBCountries country) {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(TLType.LOTL);
        trustedList.setTerritory(country);
        trustedList.setXmlFile(new DBFiles());
        trustedList.setStatus(TLStatus.PROD);
        trustedList.setArchive(archive);
        repo.save(trustedList);
        return trustedList.getId();
    }
}
