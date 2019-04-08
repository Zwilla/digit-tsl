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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class RulesRunnerServiceTest extends AbstractSpringTest {

    @Autowired
    private RulesRunnerService rulesRunnerService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private CheckService checkService;

    @Test
    public void analyzeTLWithPrevious() throws Exception {

        int previousTLId = createTLinDB(TLType.TL);
        TL previous = tlBuilder.buildTLV4(previousTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-38.xml")));

        int currentTLId = createTLinDB(TLType.TL);
        TL current = tlBuilder.buildTLV4(currentTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-39-bis.xml")));
        rulesRunnerService.runAllRules(current, previous);

        List<CheckDTO> checkResults = checkService.getTLChecksResult(currentTLId);
        assertTrue(CollectionUtils.isNotEmpty(checkResults));

    }

    @Test
    public void compareTLWithPrevious() throws Exception {

        int previousTLId = createTLinDB(TLType.TL);
        TL previous = tlBuilder.buildTLV4(previousTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-38.xml")));

        int currentTLId = createTLinDB(TLType.TL);
        TL current = tlBuilder.buildTLV4(currentTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-39-bis.xml")));

        rulesRunnerService.compareTL(current, previous);

        List<CheckDTO> checkResults = checkService.getTLChecksResult(currentTLId);
        assertTrue(!CollectionUtils.isEmpty(checkResults));
        assertTrue(checkResults.get(0).getId().equals("2_SCHEME_INFORMATION_SEQUENCE_NUMBER"));

    }

    @Test
    public void compareSK() throws Exception {
        int previousTLId = createTLinDB(TLType.TL);
        TL previous = tlBuilder.buildTLV4(previousTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/SK/2016-10-13_13-05-40.xml")));
        List<TLDifference> diffs = new ArrayList<>();
        for (TLServiceProvider tsp : previous.getServiceProviders()) {
            tsp.asPublishedDiff(tsp, tsp.getId());
            for (TLServiceDto service : tsp.getTSPServices()) {
                for (TLDigitalIdentification dit : service.getDigitalIdentification()) {
                    diffs.addAll(dit.asPublishedDiff(dit));
                }
            }
        }
    }

    private int createTLinDB(TLType type) {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(type);
        trustedList.setXmlFile(new DBFiles());
        trustedList.setStatus(TLStatus.PROD);
        tlRepository.save(trustedList);
        return trustedList.getId();
    }

    // TODO(5.4.RC1) : Implement integration test
    // @Test
    // public void analyzeLOTLWithoutPrevious() throws Exception {
    //
    // int lotlId = createTLinDB(TLType.LOTL);
    //
    // TrustStatusListType tsl = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));
    // TL tl = tlBuilder.buildTL(lotlId, tsl);
    // rulesRunnerService.runAllRules(tl, null);
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(lotlId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    // }

    // @Test
    // public void analyzeTL() throws Exception {
    //
    // int lotlId = createTLinDB(TLType.TL);
    //
    // TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml"));
    // TL tl = tlBuilder.buildTLV5(lotlId, tsl);
    // System.out.println("************** runAllRules for TL : " + tl.getServiceProviders().size());
    // rulesRunnerService.runAllRules(tl, null);
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(lotlId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    //
    // rulesRunnerService.runAllRules(tl, null);
    // }

    // @Test
    // public void analyzeTLServiceProvider() throws Exception {
    //
    // int previousTLId = createTLinDB(TLType.TL);
    // TL previous = tlBuilder.buildTL(previousTLId, jaxbService.unmarshallTSL(new
    // File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-39-bis.xml")));
    //
    // rulesRunnerService.validateAllServiceProvider(previousTLId, previous.getServiceProviders());
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(previousTLId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    //
    // }

    // @Test
    // public void analyzeTLPointers() throws Exception {
    // int previousTLId = createTLinDB(TLType.TL);
    // rulesRunnerService.validateAllPointers(previousTLId);
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(previousTLId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    //
    // }

}
