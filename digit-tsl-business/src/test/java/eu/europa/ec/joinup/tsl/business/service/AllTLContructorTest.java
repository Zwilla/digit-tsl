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
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import eu.europa.ec.joinup.tsl.business.PersistenceTestConfig;
import eu.europa.ec.joinup.tsl.business.ServiceTestConfig;
import eu.europa.ec.joinup.tsl.business.config.BusinessConfig;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

@RunWith(Theories.class)
@ContextConfiguration(classes = {
        BusinessConfig.class, PersistenceTestConfig.class, ServiceTestConfig.class
})
public class AllTLContructorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllTLContructorTest.class);

    @DataPoints
    public static File[] data() {
        File folder = new File("src/test/resources/tsl");
        Collection<File> listFiles = FileUtils.listFiles(folder, new String[] {
                "xml"
        }, true);

        return listFiles.toArray(new File[listFiles.size()]);
    }

    private TestContextManager testContextManager;

    @Autowired
    TrustedListJaxbService jaxbService;

    @Autowired
    TLBuilder tlBuilder;

    @Before
    public void setUpStringContext() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    @Theory
    public void testConstructor(File fileTotest) throws Exception {
        LOGGER.info("File to test : " + fileTotest);
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(fileTotest);

        TL myTl = null;
        if (unmarshall.getSchemeInformation() != null) {
            myTl = tlBuilder.buildTLV4(0, unmarshall);
        }

        assertTrue(StringUtils.isNotEmpty(myTl.getTslTag()));
        assertTrue(myTl.getSchemeInformation() != null);
        assertTrue(StringUtils.isNotEmpty(myTl.getSchemeInformation().getTerritory()));
        assertTrue(myTl.getSchemeInformation().getSequenceNumber() > 0);
        assertTrue(myTl.getSchemeInformation().getTlIdentifier() > 0);

        if (myTl.getPointers() != null) {
            assertTrue(myTl.getPointers().size() > 0);
            for (int i = 0; i < myTl.getPointers().size(); i++) {
                assertTrue(StringUtils.isNotEmpty(myTl.getPointers().get(i).getTlLocation()));
                assertTrue(StringUtils.isNotEmpty(myTl.getPointers().get(i).getSchemeTerritory()));
            }
        }

        if (CollectionUtils.isNotEmpty(myTl.getServiceProviders())) {
            for (int i = 0; i < myTl.getServiceProviders().size(); i++) {
                assertTrue(myTl.getServiceProviders().get(i).getTSPName().size() > 0);
                assertTrue(myTl.getServiceProviders().get(i).getTSPPostal().size() > 0);
                assertTrue(myTl.getServiceProviders().get(i).getTSPElectronic().size() > 0);
            }
        }

    }

}
