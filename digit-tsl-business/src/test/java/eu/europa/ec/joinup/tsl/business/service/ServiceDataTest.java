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
package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.TLDataTypeCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.TSPDataDTO;
import eu.europa.ec.joinup.tsl.business.repository.ServiceRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBService;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

//TODO(Simon) : replace loadTL by manual persistence with local file
public class ServiceDataTest extends AbstractSpringTest {

    @Autowired
    private TLDataService tlDataService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TLDataLoaderService dataLoaderService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Before
    public void init() {
        loadTL(countryService.getCountryByTerritory("BE"), TLType.TL);
        loadTL(countryService.getCountryByTerritory("PL"), TLType.TL);
    }

    @After
    public void destroy() {
        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("BE");
        tlDataService.deleteDataByCountry("PL");
    }

    @Test
    public void verifyLOTLUrl() {
        Assert.assertEquals("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml", applicationPropertyService.getLOTLUrl());

        applicationPropertyService.updateLOTLUrl("TEST");
        Assert.assertEquals("TEST", applicationPropertyService.getLOTLUrl());
    }

    @Test
    public void automaticLOTLUpdate() {
        applicationPropertyService.updateLOTLUrl("TEST");
        Assert.assertEquals("TEST", applicationPropertyService.getLOTLUrl());

        loadTL(countryService.getCountryByTerritory("EU"), TLType.LOTL);
        dataLoaderService.updateLOTLLocation();
        Assert.assertEquals("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml", applicationPropertyService.getLOTLUrl());
    }

    @Test
    public void searchService() {
        List<DBService> servicesBE = serviceRepository.findAllByCountryCode("BE");
        Assert.assertNotNull(servicesBE);
        List<DBService> servicesPL = serviceRepository.findAllByCountryCode("PL");
        Assert.assertNotNull(servicesPL);
        Assert.assertEquals(0, serviceRepository.findAllByCountryCode("DE").size());
        // Belgium
        List<String> countries = new ArrayList<>();
        countries.add("BE");

        List<String> serviceTypes = new ArrayList<>();
        serviceTypes.add("QCertESig");

        TLDataTypeCriteriaDTO criteria = new TLDataTypeCriteriaDTO(countries, serviceTypes);

        List<ServiceDataDTO> resultBE = tlDataService.searchServiceByType(criteria);
        Assert.assertNotNull(resultBE);
        Assert.assertEquals(10, resultBE.size());

        // PL
        countries = new ArrayList<>();
        countries.add("PL");

        serviceTypes = new ArrayList<>();
        serviceTypes.add("CertESig");

        criteria = new TLDataTypeCriteriaDTO(countries, serviceTypes);

        List<ServiceDataDTO> resultPL = tlDataService.searchServiceByType(criteria);
        Assert.assertNotNull(resultPL);
        Assert.assertEquals(6, resultPL.size());

        serviceTypes = new ArrayList<>();
        serviceTypes.add("QTimestamp");
        criteria = new TLDataTypeCriteriaDTO(countries, serviceTypes);

        resultPL = tlDataService.searchServiceByType(criteria);
        Assert.assertNotNull(resultPL);
        Assert.assertEquals(27, resultPL.size());
    }

    @Test
    public void searchTrustService() {
        List<String> countries = new ArrayList<>();
        countries.add("BE");

        List<String> serviceTypes = new ArrayList<>();
        serviceTypes.add("QCertESig");

        TLDataTypeCriteriaDTO criteria = new TLDataTypeCriteriaDTO(countries, serviceTypes);

        List<TSPDataDTO> resultBE = tlDataService.searchTrustServiceProvider(criteria);
        Assert.assertNotNull(resultBE);
        Assert.assertEquals(4, resultBE.size());

    }

    private void loadTL(DBCountries country, TLType type) {
        DBTrustedLists dbTL = new DBTrustedLists();
        DBFiles xmlFile = new DBFiles();
        xmlFile.setLocalPath("TL - " + country.getCodeTerritory() + ".xml");
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
        dbTL.setType(type);
        tlRepository.save(dbTL);

        dataLoaderService.updateTrustedListData(country);

    }

}
