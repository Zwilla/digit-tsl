package eu.europa.ec.joinup.tsl.business.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.repository.FileRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class DBFileTest extends AbstractSpringTest {

    @Autowired
    private FileRepository fileRepo;

    private static final String EU_COUNTRY_CODE = "EU";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TLRepository tlRepo;

    @Autowired
    private CountryService countryService;

    @Before
    public void init() {
        Load load = new Load();
        tlLoader.loadTL(EU_COUNTRY_CODE, getLOTLUrl(), TLType.LOTL, TLStatus.PROD, load);
    }

    @Test
    public void test() {
        DBCountries territory = countryService.getCountryByTerritory("EU");
        DBTrustedLists dbTl = tlService.getPublishedDbTLByCountry(territory);
        dbTl.setArchive(true);
        tlRepo.save(dbTl);

        Load load = new Load();
        tlLoader.loadTL(EU_COUNTRY_CODE, getLOTLUrl(), TLType.LOTL, TLStatus.PROD, load);

        List<DBFiles> files = fileRepo.findByPublishedTerritoryOrderByFirstScanDate(territory);
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.size());
    }
}
