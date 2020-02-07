package eu.europa.ec.joinup.tsl.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europa.ec.joinup.tsl.business.config.AsyncConfig;
import eu.europa.ec.joinup.tsl.business.config.BusinessConfig;
import eu.europa.ec.joinup.tsl.business.config.ExecutorServiceConfig;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.repository.ServiceRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLCertificateRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.repository.TrustServiceProviderRepository;
import eu.europa.ec.joinup.tsl.business.service.ApplicationPropertyService;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.business.service.TLBuilder;
import eu.europa.ec.joinup.tsl.business.service.TLDataLoaderService;
import eu.europa.ec.joinup.tsl.business.service.TrustedListJaxbService;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AsyncConfig.class, ExecutorServiceConfig.class, BusinessConfig.class, AlertingTestConfig.class, PersistenceTestConfig.class, ServiceTestConfig.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public abstract class AbstractSpringTest {

    @Autowired
    private TLCertificateRepository certificateRepository;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TrustServiceProviderRepository trustServiceRepository;

    @Autowired
    private TLDataLoaderService dataLoaderService;
    @Autowired
    private CountryService countryService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    public void loadAllTLs(String prefix) {
        tlRepository.deleteAll();
        trustServiceRepository.deleteAll();
        serviceRepository.deleteAll();
        certificateRepository.deleteAll();

        for (DBCountries country : countryService.getAll()) {
            if (!country.getCodeTerritory().equals("EU")) {
                loadTL(country, prefix + "TL - " + country.getCodeTerritory());
            }
        }
    }

    public void loadTL(DBCountries country, String name) {
        DBTrustedLists dbTL = new DBTrustedLists();
        DBFiles xmlFile = new DBFiles();
        xmlFile.setLocalPath(name + ".xml");
        xmlFile.setLastScanDate(new Date());
        xmlFile.setLastScanDate(new Date());
        xmlFile.setDigest("aa");
        xmlFile.setMimeTypeFile(MimeType.XML);
        dbTL.setXmlFile(xmlFile);

        dbTL.setArchive(false);
        dbTL.setName(country.getCodeTerritory());
        dbTL.setVersionIdentifier(5);
        dbTL.setNextUpdateDate(new Date());
        dbTL.setSequenceNumber(100);
        dbTL.setTerritory(country);
        dbTL.setStatus(TLStatus.PROD);
        dbTL.setType(TLType.TL);
        tlRepository.save(dbTL);

        dataLoaderService.updateTrustedListData(country);

    }

    public String getLOTLUrl() {
        return applicationPropertyService.getLOTLUrl();
    }

    public TL fileToTL(int id, String path) throws IOException {
        InputStream xml = new FileInputStream(new File(path));
        TrustStatusListTypeV5 unmarshall = jaxbService.unmarshallTSLV5(xml);
        return tlBuilder.buildTLV5(id, unmarshall);
    }

}
