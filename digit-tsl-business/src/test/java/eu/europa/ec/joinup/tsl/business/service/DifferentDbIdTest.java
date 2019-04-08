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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.AbstractTLDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLAdditionnalServiceInfo;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class DifferentDbIdTest extends AbstractSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DifferentDbIdTest.class);

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void testConstructor() throws Exception {
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));
        TL tl = tlBuilder.buildTLV4(1, unmarshall);
        checkTL(tl);

        unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/tsl/DE/2016-10-13_12-55-02.xml"));
        tl = tlBuilder.buildTLV4(1, unmarshall);
        checkTL(tl);
    }

    private void checkTL(TL tl) {
        List<String> ids = new ArrayList<>();
        assertNotNull(tl);

        fillIds(tl, ids);

        assertTrue(CollectionUtils.isNotEmpty(ids));
        LOGGER.info("NB different ids : " + ids.size());

    }

    private void fillIds(TL tl, List<String> ids) {
        addId(ids, tl.getId());

        TLSchemeInformation schemeInformation = tl.getSchemeInformation();
        addId(ids, schemeInformation.getId());
        checkDTOIds(ids, schemeInformation.getDistributionPoint());
        checkDTOIds(ids, schemeInformation.getSchemeInfoUri());
        checkDTOIds(ids, schemeInformation.getSchemeName());
        checkDTOIds(ids, schemeInformation.getSchemeOpeElectronic());
        checkDTOIds(ids, schemeInformation.getSchemeOpeName());
        checkDTOIds(ids, schemeInformation.getSchemeOpePostal());
        checkDTOIds(ids, schemeInformation.getSchemePolicy());
        checkDTOIds(ids, schemeInformation.getSchemeTypeCommRule());

        List<TLPointersToOtherTSL> pointers = tl.getPointers();
        for (TLPointersToOtherTSL tlPointersToOtherTSL : pointers) {
            addId(ids, tlPointersToOtherTSL.getId());

            checkDTOIds(ids, tlPointersToOtherTSL.getSchemeOpeName());
            checkDTOIds(ids, tlPointersToOtherTSL.getSchemeTypeCommunity());
            checkTLServiceDigitalIdentifications(ids, tlPointersToOtherTSL.getServiceDigitalId());
        }

        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        for (TLServiceProvider tlServiceProvider : serviceProviders) {
            addId(ids, tlServiceProvider.getId());

            checkDTOIds(ids, tlServiceProvider.getTSPElectronic());
            checkDTOIds(ids, tlServiceProvider.getTSPInfoUri());
            checkDTOIds(ids, tlServiceProvider.getTSPName());
            checkDTOIds(ids, tlServiceProvider.getTSPPostal());

            List<TLServiceDto> tspServices = tlServiceProvider.getTSPServices();
            for (TLServiceDto tlService : tspServices) {
                addId(ids, tlService.getId());
                checkTLServiceDigitalIdentifications(ids, tlService.getDigitalIdentification());
                checkTLServiceExtensions(ids, tlService.getExtension());

                List<TLServiceHistory> histories = tlService.getHistory();
                if (CollectionUtils.isNotEmpty(histories)) {
                    for (TLServiceHistory tlServiceHistory : histories) {
                        addId(ids, tlServiceHistory.getId());
                        checkTLServiceDigitalIdentifications(ids, tlServiceHistory.getDigitalIdentification());
                        checkTLServiceExtensions(ids, tlServiceHistory.getExtension());
                        checkDTOIds(ids, tlServiceHistory.getServiceName());
                    }
                }

                checkDTOIds(ids, tlService.getSchemeDefinitionUri());
                checkDTOIds(ids, tlService.getServiceName());
                checkDTOIds(ids, tlService.getSupplyPoint());
                checkDTOIds(ids, tlService.getTSPDefinitionUri());
            }

            checkDTOIds(ids, tlServiceProvider.getTSPTradeName());
        }
    }

    private void checkDTOIds(List<String> ids, List<? extends AbstractTLDTO> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            for (AbstractTLDTO dto : dtos) {
                addId(ids, dto.getId());
            }
        }
    }

    private void checkTLServiceExtensions(List<String> ids, List<TLServiceExtension> extensions) {
        if (CollectionUtils.isNotEmpty(extensions)) {
            for (TLServiceExtension tlServiceExtension : extensions) {
                addId(ids, tlServiceExtension.getId());
                TLAdditionnalServiceInfo additionnalServiceInfo = tlServiceExtension.getAdditionnalServiceInfo();
                if (additionnalServiceInfo != null) {
                    addId(ids, additionnalServiceInfo.getId());
                }
                checkDTOIds(ids, tlServiceExtension.getQualificationsExtension());
            }
        }
    }

    private void checkTLServiceDigitalIdentifications(List<String> ids, List<TLDigitalIdentification> digitalIdentifications) {
        if (CollectionUtils.isNotEmpty(digitalIdentifications)) {
            for (TLDigitalIdentification tlServiceDigitalIdentification : digitalIdentifications) {
                // addId(ids, tlServiceDigitalIdentification.getId());
                addId(ids, tlServiceDigitalIdentification.getId());
            }
        }
    }

    private void addId(List<String> ids, String id) {
        assertFalse(ids.contains(id));
        ids.add(id);
    }

}
