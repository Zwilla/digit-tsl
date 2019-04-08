/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business;

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
import eu.europa.ec.joinup.tsl.business.repository.ServiceRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLCertificateRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.repository.TrustServiceProviderRepository;
import eu.europa.ec.joinup.tsl.business.service.ApplicationPropertyService;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.business.service.TLDataLoaderService;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AsyncConfig.class, ExecutorServiceConfig.class, BusinessConfig.class, AlertingTestConfig.class, PersistenceTestConfig.class, ServiceTestConfig.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public abstract class AbstractSpringTest {

    @Autowired
    private TLCertificateRepository certificateRepository;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private TLDataLoaderService dataLoaderService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TrustServiceProviderRepository trustServiceRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

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

}
