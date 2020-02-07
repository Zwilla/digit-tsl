package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class TLMainTest extends AbstractSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLMainTest.class);

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Test
    public void testConstructor() throws Exception {
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/TSL-DE.xml"));

        TL myTl = null;
        if (unmarshall.getSchemeInformation() != null) {
            myTl = tlBuilder.buildTLV4(0, unmarshall);
        }

        assert myTl != null;
        assertTrue(StringUtils.isNotEmpty(myTl.getTslTag()));
        assertTrue(StringUtils.isNotEmpty(myTl.getId()));
        assertNotNull(myTl.getSchemeInformation());
        assertTrue(StringUtils.isNotEmpty(myTl.getSchemeInformation().getTerritory()));
        assertTrue(myTl.getSchemeInformation().getSequenceNumber() > 0);
        assertTrue(myTl.getSchemeInformation().getTlIdentifier() > 0);

        if (myTl.getPointers() != null) {
            LOGGER.info("Pointers Found");
            assertTrue(myTl.getPointers().size() > 0);
            for (int i = 0; i < myTl.getPointers().size(); i++) {
                assertTrue(StringUtils.isNotEmpty(myTl.getPointers().get(i).getTlLocation()));
                assertTrue(StringUtils.isNotEmpty(myTl.getPointers().get(i).getSchemeTerritory()));
            }
        }

        if (myTl.getServiceProviders() != null) {
            LOGGER.info("Providers Found");
            assertTrue(myTl.getServiceProviders().size() > 0);
            for (int i = 0; i < myTl.getServiceProviders().size(); i++) {
                assertTrue(myTl.getServiceProviders().get(i).getTSPName().size() > 0);
                assertTrue(myTl.getServiceProviders().get(i).getTSPPostal().size() > 0);
                assertTrue(myTl.getServiceProviders().get(i).getTSPElectronic().size() > 0);
            }
        }

    }

}
